package com.daimler.vehicle.agent.publishingagent.service;

import com.daimler.vehicle.agent.publishingagent.dto.VehicleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessagePublisherServiceImpl implements MessagePublisherService{

    private final Logger logger = LoggerFactory.getLogger(MessagePublisherServiceImpl.class);

    private static boolean isLidOpen = false;

    private static String latestState = null;

    private static String latestDistrict = null;

    @Value("${vehicleId}")
    private Long vehicleId;

    @Value("${district}")
    private String district;

    @Value("${state}")
    private String state;

    @Value("${TOPIC}")
    private String TOPIC;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * This API will be invoked externally by other service on the event of lid is open or closed.
     * This Agent will be residing at the vehicle. Hence vehicle will have its vehicleid.
     * District is from the place where car was purchased
     * @param lidOpenStatus - provides the latest status of the lid
     * @param providedDistrict - last known district location of vehicle
     * @param providedState - last know state location of vehicle
     */
    @Override
    public void sendMessage(boolean lidOpenStatus, String providedDistrict, String providedState){
        isLidOpen = lidOpenStatus;

        //Taking default location if provided location is empty string
        latestDistrict = getLatestDistrict(providedDistrict);
        latestState = getLatestState(providedState);

        //TODO :: need to change from new Date() to system.nanoTime
        publish( new VehicleDTO(vehicleId, isLidOpen, latestDistrict, latestState, new Date()));
    }


    /**
     * publishing messaged every 2 minutes
     */
    @Scheduled(cron = "0 0/2 * * * *")
    public void sendHeartBeatMessage(){
        logger.info(" ========== SENDING HEARTHBEAT MESSAGE ========");
        publish(new VehicleDTO(vehicleId, isLidOpen, getLatestDistrict(latestDistrict), getLatestState(latestState), new Date()));
    }

    private void publish(VehicleDTO vehicleInfo){
        StringBuilder message = new StringBuilder("Sending Message = ").append(vehicleInfo);
        logger.info(message.toString());
        kafkaTemplate.send(TOPIC,vehicleInfo);
    }

    private String getLatestDistrict(String str){
        return (str == null || str.length()<1)?district:str;
    }

    private String getLatestState(String str){
        return (str == null || str.length()<1)?state:str;
    }
}
