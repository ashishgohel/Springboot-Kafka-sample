package com.daimler.ingestion.service.ingestionengine.service;


import com.daimler.ingestion.service.ingestionengine.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.util.HashMap;


public class CacheService {

    private static final Logger logger = LoggerFactory.getLogger(CacheService.class);

    public static HashMap<String,String> STATES = new HashMap<>();

    public static HashMap<String,Double> STATE_FUEL_PRICE_CACHE = new HashMap<>();

//    @Async
    public static void setFuelPriceCache() {
        if(STATES.size()>0)
            STATES.forEach((k,v) -> CacheService.STATE_FUEL_PRICE_CACHE.putIfAbsent(k, Util.randomNumber()));
        logger.info("Sample Fuel dataset for states is generated = "+CacheService.STATE_FUEL_PRICE_CACHE.toString());
    }

    /**
     * This is the worst case scenario when external service's server is down
     * @param state
     * @return
     */
    public static Double getStateCacheValue(String state){
        logger.info(" Explicitly setting the state value and mock price for it");
        String stat = Util.sanitizeString(state).toUpperCase();
        CacheService.STATES.putIfAbsent(stat, state);
        Double price = Util.randomNumber();
        STATE_FUEL_PRICE_CACHE.putIfAbsent(stat,price);
        return price;
    }

}
