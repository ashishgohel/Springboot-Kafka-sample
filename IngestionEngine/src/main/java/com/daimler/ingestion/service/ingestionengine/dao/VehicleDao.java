package com.daimler.ingestion.service.ingestionengine.dao;

import java.util.Date;

public interface VehicleDao {

    public Date getVehicleStartingTime(Long vehicleId);

    public void saveVehicleData(Long vehicleId, Date reportingDate);

    public boolean isVehicleIdPresent(Long vehicleId);

    public void deleteFromVehicleData(Long vehicleId);

}
