package com.daimler.ingestion.service.ingestionengine.service;

import com.daimler.ingestion.service.ingestionengine.dto.FuelPriceDTO;
import com.daimler.ingestion.service.ingestionengine.util.Util;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class ExternalServiceImpl implements ExternalService{

    private static final Logger logger = LoggerFactory.getLogger(ExternalServiceImpl.class);

    @Autowired
    private OkHttpClient client;

    @Value("${states.url}")
    private String STATES_URL;

    @Value("${domain.url}")
    private String DOMAIN_URL;

    @Value("${fuel.price.url}")
    private String FUEL_URL;

    @Override
    public List<String> getStates(){
        List<String> result = null;
        Response response = null;
        try{
            Request request = new Request.Builder().url(DOMAIN_URL+STATES_URL).build();
            response = client.newCall(request).execute();
            result = mapper(response);
        }catch (Exception e){
            logger.error("ExternalServiceImpl :: getStates() :: ERROR Encountered while fetching states "+e.getMessage());
        }finally {
            response.close();
        }
        logger.info("States = "+result);
        return result;
    }

    private List<String> mapper(Response response){
        ObjectMapper mapper = new ObjectMapper();
        List<String> values = null;
        try{
            values = mapper.readValue(response.body().byteStream(), ArrayList.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return values;
    }

    @Override
    public Double getFuelPrice(String district, String state){
        Double fuelPrice = null;
        StringBuilder url = new StringBuilder(DOMAIN_URL).append(FUEL_URL).append("/").append(CacheService.STATES.get(Util.sanitizeString(state).toUpperCase()));
        logger.info("url="+url);
        Request request = new Request.Builder().url(url.toString()).build();
        Response response = null;
        try{
            response = client.newCall(request).execute();
            ObjectMapper mapper = new ObjectMapper();
            ArrayList<FuelPriceDTO> fuelPriceDTOS = null;
            fuelPriceDTOS = mapper.readValue(response.body().string(), new TypeReference<ArrayList<FuelPriceDTO>>() {});

            for (FuelPriceDTO dto:fuelPriceDTOS) {
                if(Util.sanitizeString(dto.getDistrict()).equalsIgnoreCase(Util.sanitizeString(district))){
                    fuelPrice = Double.parseDouble(dto.getProducts().get(0).getProductPrice());
                    break;
                }
            }
        }catch (Exception e){
            logger.error("ExternalServiceImpl :: getFuelPrice() :: ERROR Encountered while fetching fuel price "+e.getMessage());
        }finally {
            response.close();
        }
        return fuelPrice;
    }
}
