package com.daimler.ingestion.service.ingestionengine.service;

import com.daimler.ingestion.service.ingestionengine.dao.FuelPriceDao;

import com.daimler.ingestion.service.ingestionengine.dao.VehicleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerServiceImpl {

    @Autowired
    private FuelPriceDao fuelPriceDao;

    @Autowired
    private VehicleDao vehicleDao;

    /**
     * Scheduler to run everyday midnight to flush the cache and populate it with updated values.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void flushCache(){
        fuelPriceDao.cacheEvict();
        try{
            Thread.sleep(2000);
        }catch (Exception e){
            e.printStackTrace();
        }
        vehicleDao.setStateFuelPrice();
    }

}
