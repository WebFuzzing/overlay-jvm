package com.webfuzzing.overlayjvm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
//import com.jayway.jsonpath.*;
//import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
//import com.jayway.jsonpath.spi.json.JsonProvider;
//import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
//import com.jayway.jsonpath.spi.mapper.MappingProvider;
import com.webfuzzing.overlayjvm.model.Action;
import com.webfuzzing.overlayjvm.model.Overlay;
import org.noear.snack4.ONode;

import java.io.File;

public class Processor {


    private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    static {
        mapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }


    public static Overlay parseOverlay(File file){
        try {
            return mapper.readValue(file, Overlay.class);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Overlay parseOverlay(String overlayContent){
        try {
            return mapper.readValue(overlayContent, Overlay.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }


    public static String applyOverlay(String openApiSchema, String overlayContent){

        Overlay overlay = parseOverlay(overlayContent);
        OpenAPIInfo schemaInfo = OpenAPIInfo.fromSchema(openApiSchema);
        ONode schema = ONode.ofJson(schemaInfo.getJson());

        for(Action a : overlay.getActions()){
            if(a.isRemoveAction()){
                handleRemove(schema, a);
            } else if(a.isUpdateAction()){
                handleUpdate(schema, a);
            } else if(a.isCopyAction()){
                handleCopy(schema, a);
            } else {
                throw new IllegalArgumentException("Invalid action definition: " + a);
            }
        }

        //give back result with same format as in input
        String result = schema.toJson();

        if(schemaInfo.getType() == Format.JSON) {
            return result;
        }
        if(schemaInfo.getType() == Format.YAML) {
            return FormatUtils.convertFromJsonToYaml(result);
        }

        throw new IllegalArgumentException("Unsupported type: " + schemaInfo.getType());
    }

    private static void handleCopy(ONode openApi, Action a) {
        //TODO
    }

    private static void handleUpdate(ONode openApi, Action a) {

        ONode selection = openApi.select(a.getTarget());
        if(selection.isValue()){
            selection.setValue(a.getUpdate().asText());
            return;
        }

        for(ONode node : selection.getArray()){
            if(node.isObject()){
                //TODO
            } else if(node.isArray()){
                //TODO
            } else {
                node.setValue(a.getUpdate().toString());
            }
        }

//        ArrayNode results = jsonpath.parse(openApi).read(a.getTarget());
//        for (JsonNode result : results) {
//            ArrayNode addResults = JsonPath.read(openApi, result.asText());
//            for (JsonNode addResult : addResults) {
//                if (addResult instanceof ObjectNode) {
//                    mergeChanges((ObjectNode)addResult, (ObjectNode)a.getUpdate());
//                } else if (addResult instanceof ArrayNode) {
//                    ((ArrayNode)addResult).add(a.getUpdate());
//                } //else the JSONPointer points at a field and this is ignored as per the spec
//            }
//        }
    }

    private static void mergeChanges(ObjectNode orig, ObjectNode updates) {
//        Iterator<String> updateFields = updates.fieldNames();
//        while (updateFields.hasNext()) {
//            String updateField = updateFields.next();
//            if(orig.get(updateField) != null && orig.get(updateField).isArray()) {
//                orig.withArray(updateField).addAll((ArrayNode)updates.get(updateField));
//            } else if (orig.get(updateField) != null && orig.get(updateField).isObject()) {
//                mergeChanges((ObjectNode)orig.get(updateField), (ObjectNode)updates.get(updateField));
//            } else {
//                orig.set(updateField, updates.get(updateField));
//            }
//        }
    }

    private static void handleRemove(ONode openApi, Action a) {
        openApi.delete(a.getTarget());
    }
}
