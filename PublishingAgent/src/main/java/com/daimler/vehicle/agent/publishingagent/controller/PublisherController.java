package com.daimler.vehicle.agent.publishingagent.controller;


import com.daimler.vehicle.agent.publishingagent.service.MessagePublisherService;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Validated
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
    public ResponseEntity sendMessage(@NotNull @RequestParam(value = "lidStatus", required = true)  boolean lidStatus, @NotEmpty @RequestParam(value = "district", required = true)  String district, @NotEmpty @RequestParam(value = "state", required = true) String state) throws Exception{
        logger.info(" PublisherController : sendMessage() : lidStatus="+lidStatus+" ::  district="+district+ " :: State = "+state);
        logger.info(" --- Sending message --- ");
        publisherService.sendMessage(lidStatus, district, state);
        return new ResponseEntity("Message Published Successfully",HttpStatus.OK);
    }

}
