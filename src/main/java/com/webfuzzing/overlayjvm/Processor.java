package com.webfuzzing.overlayjvm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.webfuzzing.overlayjvm.pojo.Overlay;

public class Processor {

    private static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    static {
        mapper.enable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
        mapper.enable(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES);
        mapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }


    public static Overlay parseOverlay(String overlayContent){
        try {
            return mapper.readValue(overlayContent, Overlay.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
