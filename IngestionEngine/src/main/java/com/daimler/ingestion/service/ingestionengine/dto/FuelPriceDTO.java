package com.daimler.ingestion.service.ingestionengine.dto;

import java.util.Arrays;
import java.util.List;

public class FuelPriceDTO {

    private String district;

    private List<ProductsDTO> products;

    public FuelPriceDTO() {
    }

    public FuelPriceDTO(String district, List<ProductsDTO> products) {
        this.district = district;
        this.products = products;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public List<ProductsDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsDTO> products) {
        this.products = products;
    }

    @Override
    public String toString(){
        return new StringBuilder("{ district:").append(district).append(", products:").append(this.products.toArray()).toString();
    }
}
