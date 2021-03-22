package com.daimler.ingestion.service.ingestionengine.service;

import java.util.List;

public interface ExternalService {

    public List<String> getStates() ;

    public Double getFuelPrice(String district, String state);
}
