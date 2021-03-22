package com.daimler.vehicle.agent.publishingagent.service;

public interface MessagePublisherService {

    public void sendMessage(boolean lidOpenStatus, String district, String state);

}
