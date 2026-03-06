package com.webfuzzing.overlayjvm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class FormatUtils {

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final YAMLMapper YAML_MAPPER = new YAMLMapper();
    private static final ObjectMapper ALL_MAPPER = new ObjectMapper(new YAMLFactory());
    private static final ObjectMapper NORMALIZING_JSON_MAPPER = new ObjectMapper();
    private static final YAMLMapper NORMALIZING_YAML_MAPPER = new YAMLMapper();

    static{
        //this does not solve the problem, as it is not recursive on POJO entries??? arghhh...
        NORMALIZING_YAML_MAPPER.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        NORMALIZING_JSON_MAPPER.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    }


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

    public static String normalizeYaml(String schema){
        try {
            JsonNode root = ALL_MAPPER.readTree(schema);
            return NORMALIZING_YAML_MAPPER.writeValueAsString(root);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String normalizeJson(String schema){
        try {
            JsonNode root = ALL_MAPPER.readTree(schema);
            return NORMALIZING_JSON_MAPPER.writeValueAsString(root);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static JsonNode readAsJson(String json) throws JsonProcessingException {
        return JSON_MAPPER.readTree(json);
    }
}
