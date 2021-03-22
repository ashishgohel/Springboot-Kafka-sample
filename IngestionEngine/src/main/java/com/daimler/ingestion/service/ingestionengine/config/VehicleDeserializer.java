package com.daimler.ingestion.service.ingestionengine.config;

import com.daimler.ingestion.service.ingestionengine.dto.VehicleDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class VehicleDeserializer implements Deserializer<VehicleDTO> {

    @Override
    public VehicleDTO deserialize(String s, byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        VehicleDTO dto = null;
        try {
            dto = mapper.readValue(bytes, VehicleDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dto;
    }
}
