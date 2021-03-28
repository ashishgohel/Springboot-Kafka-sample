package com.daimler.vehicle.agent.publishingagent.security;

import com.daimler.vehicle.agent.publishingagent.controller.PublisherController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import javax.validation.ConstraintViolationException;


@RestControllerAdvice(basePackages = "com.daimler.vehicle.agent.publishingagent.controller")
public class ControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> invalidParamsExceptionHandler(ConstraintViolationException e){
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> invalidParamsExceptionHandler(MethodArgumentNotValidException e){
        logger.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }


}
