package com.daimler.ingestion.service.ingestionengine.dto;

public class ProductsDTO {

    private String productName;

    private String productPrice;

    private String productCurrency;

    private String priceChange;

    private String priceChangeSign;

    public ProductsDTO(String productName, String productPrice, String productCurrency, String priceChange, String priceChangeSign) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productCurrency = productCurrency;
        this.priceChange = priceChange;
        this.priceChangeSign = priceChangeSign;
    }

    public ProductsDTO() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductCurrency() {
        return productCurrency;
    }

    public void setProductCurrency(String productCurrency) {
        this.productCurrency = productCurrency;
    }

    public String getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(String priceChange) {
        this.priceChange = priceChange;
    }

    public String getPriceChangeSign() {
        return priceChangeSign;
    }

    public void setPriceChangeSign(String priceChangeSign) {
        this.priceChangeSign = priceChangeSign;
    }

    @Override
    public String toString(){
        return new StringBuilder("{ ProductName:").append(this.productName).append(", productPrice:").append(this.productPrice).append(", productCurrency:").append(this.productCurrency).append(",priceChange:").append(this.priceChange).append(",priceChangeSign:").append(this.priceChangeSign).append("}").toString();
    }
}
