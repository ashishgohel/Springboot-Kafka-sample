package com.daimler.ingestion.service.ingestionengine.dto;

public class DistictStateDTO {

    private String state;

    private String district;

    public DistictStateDTO(String state, String district) {
        this.state = state;
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public String toString(){
        return  new StringBuilder("{ state:").append(this.state).append(", district:").append(this.district).append("}").toString();
    }
}
