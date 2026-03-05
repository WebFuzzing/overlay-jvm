package com.webfuzzing.overlayjvm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class OpenAPIInfo {



    private final String original;

    private final String json;

    private final Format format;

    public OpenAPIInfo(String original, String json, Format format) {
        this.original = original;
        this.json = json;
        this.format = format;
    }

    public String getOriginal() {
        return original;
    }

    public String getJson() {
        return json;
    }

    public Format getType() {
        return format;
    }

    public static OpenAPIInfo fromSchema(String schema){

        try {
            FormatUtils.readAsJson(schema);
            return new OpenAPIInfo(schema,schema, Format.JSON);
        } catch (Exception e) {

            try {
                String json = FormatUtils.convertFromYamlToJson(schema);
                return new OpenAPIInfo(schema,json, Format.YAML);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Cannot parse schema", ex);
            }
        }
    }


}
