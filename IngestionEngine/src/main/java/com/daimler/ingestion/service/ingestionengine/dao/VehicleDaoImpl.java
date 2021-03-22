package com.daimler.ingestion.service.ingestionengine.dao;

import com.daimler.ingestion.service.ingestionengine.dto.FuelPriceDTO;
import com.daimler.ingestion.service.ingestionengine.dto.DistictStateDTO;
import com.daimler.ingestion.service.ingestionengine.util.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Component
public class VehicleDaoImpl implements VehicleDao {

    private static final Logger logger = LoggerFactory.getLogger(VehicleDaoImpl.class);

    @Autowired
    private OkHttpClient client;

    @Value("${states.url}")
    private String STATES_URL;

    @Value("${districts.url}")
    private String DISTRICT_URL;

    @Value("${domain.url}")
    private String DOMAIN_URL;

    @Value("${fuel.price.url}")
    private String FUEL_URL;

    @PostConstruct
    public void init(){
        try{
            getStates();
            Thread.sleep(1500);
        }catch (Exception e){
            logger.error("ERROR generated while fetching STATE details" + e );
        }
        populateDistrictPrices();
    }

    private static HashMap<Long, Date> VEHICLE_DATA_CACHE = new HashMap<>();

    private static HashMap<String, DistictStateDTO> DISTRICT_STATE_MAP = new HashMap<>();

    private static HashMap<String,String> STATES = new HashMap<>();

    //Stores VehicleId and session Id. It is a mock data of vehicle ID but authenticates and provides session to the legitimate vehicle
    private static HashMap<String,String> VEHICLE_AUTH_MAP = new HashMap<>();

    public Date getVehicleStartingTime(Long vehicleId){
        return VEHICLE_DATA_CACHE.get(vehicleId);
    }

    public void saveVehicleData(Long vehicleId, Date reportingDate){
        VEHICLE_DATA_CACHE.putIfAbsent(vehicleId, reportingDate);
    }

    public boolean isVehicleIdPresent(Long vehicleId){
        return getVehicleStartingTime(vehicleId) != null ? true : false ;
    }

    public void deleteFromVehicleData(Long vehicleId){
        VEHICLE_DATA_CACHE.remove(vehicleId);
    }

    private void populateDistrictAndStates(){
        try {
            if(STATES.size()>0){
                for (String state:STATES) {
                    Thread.sleep(1000);
                    for(String district:getDistricts(state)){
                        try{
                            DISTRICT_STATE_MAP.putIfAbsent(Util.sanitizeString(district).toUpperCase().replaceAll(" ", ""),new DistictStateDTO(state, district));
                        }catch (Exception e){
                            logger.error("ERROR generated while fetching DISTRICT details" + e );
                            logger.info(" Skipping - As to unable to fetch District of State ="+state);
                            continue;
                        }
                    }
                }
            }

        } catch (Exception e) {
//            e.printStackTrace();
            logger.error("ERROR generated while fetching details" + e );
        }
        logger.info("populateDistrictAndStates():: District and States are populated = "+DISTRICT_STATE_MAP.toString());
    }

    @Override
    public void setStateFuelPrice(){
        FuelPriceDaoImpl.setFuelPriceCache(STATES);
    }

    private void getStates() throws IOException {
        Request request = new Request.Builder().url(DOMAIN_URL+STATES_URL).build();
        Response response = client.newCall(request).execute();

        List<String> values = mapper(response);
        response.close();
        logger.info("State = "+values);
        for (String state:values)
            STATES.putIfAbsent(Util.sanitizeString(state).toUpperCase(), state);

//        STATES.addAll(values);
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

    private List<String> getDistricts(String state) throws Exception{
        Thread.sleep(1000);
        StringBuilder url = new StringBuilder(DOMAIN_URL).append("/").append(state).append(DISTRICT_URL);
        logger.info("URL - "+url);
        Request request = new Request.Builder().url(url.toString()).build();
        Response response = client.newCall(request).execute();

        List<String> values = mapper(response);
        response.close();
        logger.info("Districts = "+values);
        return values;
    }

    /*public Double getFuelPriceByDistrict(String district){
        DistictStateDTO dto = DISTRICT_STATE_MAP.get(district.toUpperCase().replaceAll(" ", ""));
        if(dto != null){

            StringBuilder url = new StringBuilder(DOMAIN_URL).append("/").append(state).append(DISTRICT_URL);
        }
    }*/

    private void populateDistrictPrices(){
        try {
            if(STATES.size()>0){
                for (Map.Entry<String,String> entry: STATES.entrySet()) {
                    Thread.sleep(1000);
                    StringBuilder url = new StringBuilder(DOMAIN_URL).append(FUEL_URL).append("/").append(entry.getValue());
                    logger.info("url="+url);
                    Request request = new Request.Builder().url(url.toString()).build();
                    Response response = client.newCall(request).execute();

                    ObjectMapper mapper = new ObjectMapper();

                    List<FuelPriceDTO> fuelPriceDTOS = null;
                    try {
                        fuelPriceDTOS = mapper.readValue(response.body().byteStream(), ArrayList.class);
                        logger.info(fuelPriceDTOS.toString());
                    }catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }

        } catch (Exception e) {
//            e.printStackTrace();
            logger.error("ERROR generated while fetching details" + e );
        }
        logger.info("populateDistrictAndStates():: District and States are populated = "+DISTRICT_STATE_MAP.toString());
    }

}
