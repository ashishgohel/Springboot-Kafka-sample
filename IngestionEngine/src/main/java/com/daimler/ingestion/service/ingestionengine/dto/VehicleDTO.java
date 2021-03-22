package com.daimler.ingestion.service.ingestionengine.dto;

import java.io.Serializable;
import java.util.Date;

public class VehicleDTO implements Serializable {

    private Long vehicleId;

    private boolean lidStatus;

    private String district;

    private Date reportingTime;

    private String state;

    public VehicleDTO() {
    }

    public VehicleDTO(Long vehicleId, boolean lidStatus, String district, String state, Date reportingTime) {
        this.vehicleId = vehicleId;
        this.lidStatus = lidStatus;
        this.district = district;
        this.state = state;
        this.reportingTime = reportingTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public Date getReportingTime() {
        return reportingTime;
    }

    public void setReportingTime(Date reportingTime) {
        this.reportingTime = reportingTime;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public boolean isLidStatus() {
        return lidStatus;
    }

    public void setLidStatus(boolean lidStatus) {
        this.lidStatus = lidStatus;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString(){
        return  new StringBuilder("{ vehicleID:").append(this.vehicleId).append(", lidStatus:").append(this.lidStatus).append(", district:").append(this.district).append(", state:").append(this.state).append(", reportingTime:").append(this.reportingTime).append("}").toString();
    }
}
