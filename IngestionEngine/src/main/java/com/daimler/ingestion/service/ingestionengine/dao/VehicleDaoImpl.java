package com.daimler.ingestion.service.ingestionengine.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class VehicleDaoImpl implements VehicleDao {

    private static final Logger logger = LoggerFactory.getLogger(VehicleDaoImpl.class);

    private static HashMap<Long, Date> VEHICLE_DATA_CACHE = new HashMap<>();

    @Override
    public Date getVehicleStartingTime(Long vehicleId){
        return VEHICLE_DATA_CACHE.get(vehicleId);
    }

    @Override
    public void saveVehicleData(Long vehicleId, Date reportingDate){
        VEHICLE_DATA_CACHE.putIfAbsent(vehicleId, reportingDate);
    }

    @Override
    public boolean isVehicleIdPresent(Long vehicleId){
        return getVehicleStartingTime(vehicleId) != null ? true : false ;
    }

    @Override
    public void deleteFromVehicleData(Long vehicleId){
        logger.info("Vehicle {} is Evicted from the cache ",vehicleId);
        VEHICLE_DATA_CACHE.remove(vehicleId);
    }

}
