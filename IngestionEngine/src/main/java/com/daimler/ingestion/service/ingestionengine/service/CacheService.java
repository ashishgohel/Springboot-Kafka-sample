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
        STATES.forEach((k,v) -> CacheService.STATE_FUEL_PRICE_CACHE.putIfAbsent(k, Util.randomNumber()));
        logger.info("Sample Fuel dataset for states is generated = "+CacheService.STATE_FUEL_PRICE_CACHE.toString());
    }

}
