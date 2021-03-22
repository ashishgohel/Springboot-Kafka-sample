package com.daimler.ingestion.service.ingestionengine.dao;



import com.daimler.ingestion.service.ingestionengine.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

import java.util.HashMap;
import java.util.List;
import java.util.Random;


@Component
public class FuelPriceDaoImpl implements FuelPriceDao{

    private static final Logger logger = LoggerFactory.getLogger(FuelPriceDaoImpl.class);

    private static HashMap<String,Float> FUEL_PRICE_CACHE = new HashMap<>();



    @PostConstruct
    public void populate(){

    }

    @Override
    @CacheEvict(cacheNames = "FuelPrice", allEntries = true)
    public void cacheEvict(){}

    @Cacheable(cacheNames = "FuelPrice")
    public Float getFuelPriceByDistrictName(String district){
        Float fuelCost = null;
        boolean isSuccess = false;
        try{
            //TODO: invoke external API
            isSuccess = true;
        }catch (Exception e){
            logger.error("ERROR occurred while fetching details from API --  "+e);
            logger.info("Fetching from Predefined populated sample fuel price");
        }
        if(!isSuccess){
            fuelCost = FUEL_PRICE_CACHE.get(district);
        }
        return fuelCost;
    }

    @Async
    public static void setFuelPriceCache(HashMap<String,String> states) {
        states.forEach((k,v) -> FUEL_PRICE_CACHE.putIfAbsent(k, randomNumber()));
        logger.info("Sample Fuel dataset for states is generated = "+FUEL_PRICE_CACHE.toString());
    }

    private static Float randomNumber(){
        Random random = new Random();
        Float val = (random.nextFloat() * (101 - 85) + 85) ;
        BigDecimal bigDecimal = new BigDecimal(Float.toString(val));
        bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bigDecimal.floatValue();
    }

    public void calculateFuelPrice(){
        //TODO:: Calculate and print fuel price for each vehicle.
    }
}
