package com.daimler.ingestion.service.ingestionengine.dao;

public interface FuelPriceDao {

    public void cacheEvict();

    public void setStateFuelPriceOnCacheRefresh();

    public Double getFuelPriceByDistrictName(String district, String state);
}
