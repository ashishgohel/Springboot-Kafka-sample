package com.daimler.vehicle.agent.publishingagent.controller;


import com.daimler.vehicle.agent.publishingagent.service.MessagePublisherService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("agent")
public class PublisherController {

    private final Logger logger = LoggerFactory.getLogger(PublisherController.class);

    @Autowired
    private MessagePublisherService publisherService;

    /**
     *
     * @param lidStatus
     * @param district
     * @param state
     */
    @PostMapping("/publish")
    public void sendMessage(@RequestParam("lidStatus") boolean lidStatus, @RequestParam("district") String district, @RequestParam("state") String state) {
        logger.info(" PublisherController : sendMessage() : lidStatus="+lidStatus+" ::  district="+district);
        logger.info(" --- Sending message --- ");
        publisherService.sendMessage(lidStatus, district, state);
    }

}
