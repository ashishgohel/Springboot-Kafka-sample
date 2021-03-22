package com.daimler.vehicle.agent.publishingagent.config;

import com.daimler.vehicle.agent.publishingagent.dto.VehicleDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

public class VehicleSerializer implements Serializer<VehicleDTO> {

    @Override
    public byte[] serialize(String s, VehicleDTO dto) {
        byte[] returnVal = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            returnVal = mapper.writeValueAsBytes(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return returnVal;
    }
}
