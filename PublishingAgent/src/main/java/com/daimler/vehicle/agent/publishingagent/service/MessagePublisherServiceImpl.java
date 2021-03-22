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
    private final String TOPIC = "my_topic";

    private static boolean isLidOpen = false;

    @Value("${vehicleId}")
    private Long vehicleId;

    @Value("${district}")
    private String district;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    /**
     * This API will be invoked externally by other service on the event of lid is open or closed.
     * This Agent will be residing at the vehicle. Hence vehicle will have its vehicleid.
     * District is from the place where car was purchased
     * @param lidOpenStatus - provides the latest status of the lid
     * @param providedDistrict - last known location of vehicle
     */
    @Override
    public void sendMessage(boolean lidOpenStatus, String providedDistrict){
        isLidOpen = lidOpenStatus;
        //Taking default location if provided location is empty string
        String dist = (providedDistrict != null && providedDistrict.length()<1)?district:providedDistrict;

        //TODO :: need to change from new Date() to system.nanoTime

        VehicleDTO vehicleInfo = new VehicleDTO(vehicleId, isLidOpen, dist, new Date());
        StringBuilder message = new StringBuilder("Sending Message = ").append(vehicleInfo);

        logger.info(message.toString());

        kafkaTemplate.send(TOPIC,vehicleInfo);
    }

    /**
     * TODO: Create Scheduler to publish message every 120 seconds
     */
    /**
     * publishing messaged every 2 minutes
     */
    @Scheduled(cron = "0 0/2 * * * *")
    public void sendHeartBeatMessage(){
        VehicleDTO vehicleInfo = new VehicleDTO(vehicleId, isLidOpen, district, new Date());
        StringBuilder message = new StringBuilder("Sending Message = ").append(vehicleInfo);
        /**
         * TODO :: Sending message
         */
        System.out.println(" ========== SENDING HEARTHBEAT MESSAGE ========");
    }
}
