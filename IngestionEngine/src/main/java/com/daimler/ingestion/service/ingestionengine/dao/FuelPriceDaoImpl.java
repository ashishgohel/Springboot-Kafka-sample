package com.daimler.ingestion.service.ingestionengine.dao;



import com.daimler.ingestion.service.ingestionengine.service.CacheService;
import com.daimler.ingestion.service.ingestionengine.service.ExternalService;
import com.daimler.ingestion.service.ingestionengine.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class FuelPriceDaoImpl implements FuelPriceDao{

    private static final Logger logger = LoggerFactory.getLogger(FuelPriceDaoImpl.class);

    @Autowired
    private ExternalService externalService;

    @Override
    @CacheEvict(cacheNames = "FuelPrice", allEntries = true)
    public void cacheEvict(){}

    @Override
    @Cacheable(cacheNames = "FuelPrice", key = "{#district,#state}")
    public Double getFuelPriceByDistrictName(String district, String state){
        Double fuelCost = null;
        boolean isSuccess = false;
        try{
            Double fuelPrice = externalService.getFuelPrice(district, state);
            if(fuelPrice != null){
                logger.info("Fuel cost for {}, {} is {} per litre",district,state,fuelPrice);
                isSuccess = true;
                fuelCost = fuelPrice;
            }
        }catch (Exception e){
            logger.error("ERROR occurred while fetching details from API --  "+e);
        }

        if(!isSuccess){
            logger.info("Fetching from Predefined populated sample fuel price");
            fuelCost = CacheService.STATE_FUEL_PRICE_CACHE.get(Util.sanitizeString(state).toUpperCase());
        }

        if(fuelCost == null)
            fuelCost = CacheService.getStateCacheValue(state);

        return fuelCost;
    }

    @Override
    public void setStateFuelPriceOnCacheRefresh(){
        CacheService.setFuelPriceCache();
    }
}
