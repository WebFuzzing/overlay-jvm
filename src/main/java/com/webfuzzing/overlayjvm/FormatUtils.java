package com.webfuzzing.overlayjvm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class FormatUtils {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final YAMLMapper YAML_MAPPER = new YAMLMapper();


    public static String convertFromJsonToYaml(String json){
        try {
            JsonNode root = JSON_MAPPER.readTree(json);
            return YAML_MAPPER.writeValueAsString(root);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String convertFromYamlToJson(String yaml){
        try {
            JsonNode root = YAML_MAPPER.readTree(yaml);
            return JSON_MAPPER.writeValueAsString(root);
        }catch (JsonProcessingException e){
            throw new IllegalArgumentException(e);
        }
    }

    public static String normalizeYaml(String yaml){
        try {
            JsonNode root = YAML_MAPPER.readTree(yaml);
            return YAML_MAPPER.writeValueAsString(root);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static JsonNode readAsJson(String json) throws JsonProcessingException {
        return JSON_MAPPER.readTree(json);
    }
}
