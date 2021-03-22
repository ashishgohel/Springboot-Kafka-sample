package com.daimler.ingestion.service.ingestionengine.service;

import com.daimler.ingestion.service.ingestionengine.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class BootstrapLoader {

    private static final Logger logger = LoggerFactory.getLogger(BootstrapLoader.class);

    @Autowired
    private ExternalService externalService;

    @PostConstruct
    public void init(){
        try{
            getStates();
        }catch (Exception e){
            logger.error("ERROR generated while fetching STATE details :: " + e.getMessage() );
        }
    }

    private void getStates() {
        for (String state: externalService.getStates()){
            CacheService.STATES.putIfAbsent(Util.sanitizeString(state).toUpperCase(), state);
        }
        CacheService.setFuelPriceCache();
    }

}
