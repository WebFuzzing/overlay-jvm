package com.webfuzzing.overlayjvm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.webfuzzing.overlayjvm.model.Action;
import com.webfuzzing.overlayjvm.model.Overlay;
import org.noear.snack4.ONode;

import java.io.File;
import java.util.Map;

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

    private static void mergeObjects(ONode x, ONode y) {
        if (!x.isObject()) {
            throw new IllegalArgumentException("Invalid object definition for x: " + x);
        }
        if (!y.isObject()) {
            throw new IllegalArgumentException("Invalid object definition for y: " + y);
        }

        for (Map.Entry<String, ONode> k : y.getObjectUnsafe().entrySet()) {
            if (!x.hasKey(k.getKey())) {
                //whole insertion, as entry was not present
                x.set(k.getKey(), k.getValue());
            } else {
                //need to merge, recursively
                if(k.getValue().isObject()) {
                    mergeObjects(x.get(k.getKey()), k.getValue());
                } else if(k.getValue().isValue()){
                    x.set(k.getKey(), k.getValue());
                } else if(k.getValue().isArray()){
                    x.get(k.getKey()).addAll(k.getValue().getArrayUnsafe());
                } else {
                    throw new IllegalArgumentException("Invalid type for: " + k.getKey());
                }
            }
        }
    }

    private static void handleUpdate(ONode openApi, Action a) {
        ONode selection = openApi.select(a.getTarget());
        if(selection.isArray() && selection.isEmpty()){
            /*
                From specs: "If the target JSONPath expression selects zero nodes,
                             the action succeeds without changing the target document."
             */
            //throw new IllegalArgumentException("JsonPath selection return no elements: " + a.getTarget());
            return;
        }

        applyUpdate(selection, a);
    }

    private static void applyUpdate(ONode selection, Action a) {
        if(selection.isValue()){
            selection.setValue(a.getUpdate().asText());
            return;
        }

        if(selection.isObject()){
            ONode update = ONode.ofJson(a.getUpdate().toString());
            mergeObjects(selection, update);
            return;
        }

        for(ONode node : selection.getArray()){
            applyUpdate(node, a);
        }
    }

    private static void handleRemove(ONode openApi, Action a) {
        openApi.delete(a.getTarget());
    }
}
