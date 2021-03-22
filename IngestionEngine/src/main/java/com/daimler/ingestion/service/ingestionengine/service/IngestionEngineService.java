package com.daimler.ingestion.service.ingestionengine.service;

import com.daimler.ingestion.service.ingestionengine.dto.VehicleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class IngestionEngineService {

    private static final Logger logger = LoggerFactory.getLogger(IngestionEngineService.class);


    @KafkaListener(topics =  "my_topic", groupId = "group-id")
    public void consume(VehicleDTO message){
        logger.info("Received Message - "+message);
    }

    //TODO:: calculate fuel consumed and how many litre petrol has been added

//    private Double getFuelPrice
}
