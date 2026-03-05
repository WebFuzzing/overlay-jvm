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

    //    private static final ParseContext jsonpath = JsonPath.using(Configuration.builder()
//            .options(Option.AS_PATH_LIST)
//            .build());

    static {
        mapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

//    static {
//        //For JsonPath
//        Configuration.setDefaults(new Configuration.Defaults() {
//
//            private final JsonProvider jsonProvider = new JacksonJsonNodeJsonProvider(mapper);
//            private final MappingProvider mappingProvider = new JacksonMappingProvider();
//
//            @Override
//            public JsonProvider jsonProvider() {
//                return jsonProvider;
//            }
//
//            @Override
//            public MappingProvider mappingProvider() {
//                return mappingProvider;
//            }
//
//            @Override
//            public Set<Option> options() {
//                return EnumSet.of(Option.ALWAYS_RETURN_LIST, Option.SUPPRESS_EXCEPTIONS);
//            }
//
//        });
//    }

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


//    private static String ensureJson(String openApiSchema){
//        try {
//            JsonNode root = mapper.readTree(openApiSchema);
//            return (new ObjectMapper()).writerWithDefaultPrettyPrinter().writeValueAsString(root);
//        } catch (JsonProcessingException e) {
//            throw new IllegalArgumentException(e);
//        }
//    }

    public static String applyOverlay(String openApiSchema, String overlayContent){

        Overlay overlay = parseOverlay(overlayContent);
        JsonNode openApi;
        try {
            openApi = mapper.readTree(openApiSchema);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }

//        String openApiJson = ensureJson(openApiSchema);
        OpenAPIInfo schemaInfo = OpenAPIInfo.fromSchema(openApiSchema);

        ONode schema = ONode.ofJson(schemaInfo.getJson());

        for(Action a : overlay.getActions()){
            if(a.isRemoveAction()){
                handleRemove(schema, a);
            } else if(a.isUpdateAction()){
                handleUpdate(openApi, a);
            } else if(a.isCopyAction()){
                handleCopy(openApi, a);
            } else {
                throw new IllegalArgumentException("Invalid action definition: " + a);
            }
        }

//        try {
//            return mapper.writeValueAsString(openApi);
//        } catch (JsonProcessingException e) {
//            throw new IllegalArgumentException(e);
//        }

        String result = schema.toJson();

        if(schemaInfo.getType() == Format.JSON) {
            return result;
        }
        if(schemaInfo.getType() == Format.YAML) {
            return FormatUtils.convertFromJsonToYaml(result);
        }

        throw new IllegalArgumentException("Unsupported type: " + schemaInfo.getType());
    }

    private static void handleCopy(JsonNode openApi, Action a) {
        //TODO
    }

    private static void handleUpdate(JsonNode openApi, Action a) {

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
//        private static void handleRemove(JsonNode openApi, Action a) {
//        Collection<JsonNode> results;
//        try {
//            results = ctx.read(a.getTarget());
//        } catch (Exception e){
//            // jayway hides the stack traces!!!!!!!!!!!!!!
//            throw new IllegalArgumentException(e.getMessage(), e);
//        }
//
//        for (JsonNode result : results) {
//            String path = result.asText();
//
//            String parent = path.substring(0, path.lastIndexOf("["));
//            ArrayNode removeResults = JsonPath.read(openApi, parent);
//            for (JsonNode toRemove : removeResults) {
//                if (toRemove instanceof ObjectNode) {
//                    String element = path.substring(path.lastIndexOf("[")+2, path.length()-2);
//                    ((ObjectNode)toRemove).remove(element);
//                } else if (toRemove instanceof ArrayNode) {
//                    int element = Integer.parseInt(path.substring(path.lastIndexOf("[")+1, path.lastIndexOf("]")));
//                    ((ArrayNode)toRemove).remove(element);
//                }
//            }
//        }
//    }
}
