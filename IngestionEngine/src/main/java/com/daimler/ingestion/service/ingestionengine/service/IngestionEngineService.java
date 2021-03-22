package com.daimler.ingestion.service.ingestionengine.service;

import com.daimler.ingestion.service.ingestionengine.dao.FuelPriceDao;
import com.daimler.ingestion.service.ingestionengine.dao.VehicleDao;
import com.daimler.ingestion.service.ingestionengine.dto.VehicleDTO;
import com.daimler.ingestion.service.ingestionengine.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class IngestionEngineService {

    private static final Logger logger = LoggerFactory.getLogger(IngestionEngineService.class);

    @Autowired
    private VehicleDao vehicleDao;

    @Autowired
    private FuelPriceDao fuelPriceDao;

    @KafkaListener(topics =  "my_topic", groupId = "group-id")
    public void consume(VehicleDTO message){
        logger.info("Received Message - "+message);

        if(message.isLidStatus()){
           vehicleDao.saveVehicleData(message.getVehicleId(), message.getReportingTime());
        }else if(vehicleDao.isVehicleIdPresent(message.getVehicleId())){
            //Fuel Lid is closed
            Date startDate = vehicleDao.getVehicleStartingTime(message.getVehicleId());
            Long secondsElapsed = Util.calculateSecondsElapsed(startDate);
            Double litresOfFuel = getNumberOfLitresFilledInTank(secondsElapsed);
            Double fuelPricePerLitre = fuelPriceDao.getFuelPriceByDistrictName(message.getDistrict(), message.getState());
            Double totalFuelPrice = getFuelCost(litresOfFuel, fuelPricePerLitre);
            logger.info(" VehicleID = {} filled {} litres of fuel at {},{}. Total cost of Fuel={} INR",message.getVehicleId(),litresOfFuel,message.getDistrict(),message.getState(),totalFuelPrice);
            vehicleDao.deleteFromVehicleData(message.getVehicleId());
        }
    }

    private Double getNumberOfLitresFilledInTank( Long seconds){
        Double val = seconds/30.0;
        return Util.roundUp(val);
    }

    private Double getFuelCost(Double numberOfLitresFilledInTank, Double fuelPricePerLitre){
        System.out.println("Number of litres ="+numberOfLitresFilledInTank+ " :: fuelPrice = "+fuelPricePerLitre );
        return Util.roundUp(numberOfLitresFilledInTank * fuelPricePerLitre);
    }
}
