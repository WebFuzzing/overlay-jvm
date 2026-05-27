package com.webfuzzing.overlayjvm;

import org.junit.jupiter.api.Test;
import org.noear.snack4.ONode;
import org.noear.snack4.jsonpath.JsonPath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class JsonPathUtilsTest {

    private final String schema = "{\n" +
            "  \"openapi\": \"3.1.0\",\n" +
            "  \"info\": {\n" +
            "    \"title\": \"Simple API\",\n" +
            "    \"version\": \"1.0.0\"\n" +
            "  },\n" +
            "  \"servers\": [\n" +
            "    {\n" +
            "      \"url\": \"https://api.example.com/v1\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"paths\": {\n" +
            "    \"/api\": {\n" +
            "      \"post\": {\n" +
            "        \"parameters\": [\n" +
            "          {\n" +
            "            \"name\": \"x\",\n" +
            "            \"in\": \"query\",\n" +
            "            \"schema\": {\n" +
            "              \"type\": \"string\"\n" +
            "            }\n" +
            "          },\n" +
            "          {\n" +
            "            \"name\": \"y\",\n" +
            "            \"in\": \"query\",\n" +
            "            \"schema\": {\n" +
            "              \"type\": \"string\"\n" +
            "            }\n" +
            "          }\n" +
            "        ],\n" +
            "        \"requestBody\": {\n" +
            "          \"required\": true,\n" +
            "          \"content\": {\n" +
            "            \"application/json\": {\n" +
            "              \"schema\": {\n" +
            "                \"type\": \"object\",\n" +
            "                \"properties\": {\n" +
            "                  \"id\": {\n" +
            "                    \"type\": \"string\"\n" +
            "                  },\n" +
            "                  \"name\": {\n" +
            "                    \"type\": \"string\"\n" +
            "                  }\n" +
            "                }\n" +
            "              }\n" +
            "            }\n" +
            "          }\n" +
            "        },\n" +
            "        \"responses\": {\n" +
            "          \"200\": {\n" +
            "            \"description\": \"Successful response\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    @Test
    public void testClosestMatch() throws Exception {

        ONode root = ONode.ofJson(schema);

        String matching = "$.paths[\"/api\"].post.parameters[?(@.name==\"x\")]";
        assertEquals(matching, JsonPathUtils.closestMatch(root, matching));

        String missingGet = "$.paths[\"/api\"].get.parameters[?(@.name==\"x\")]";
        assertEquals("$.paths[\"/api\"]", JsonPathUtils.closestMatch(root, missingGet));

        String differentTree = "$.foo";
        assertEquals("$", JsonPathUtils.closestMatch(root, differentTree));
    }

    @Test
    public void testRoot(){
        ONode root = ONode.ofJson(schema);
        assertTrue(root.exists("$"));
        JsonPath path = JsonPath.parse("$");
        assertTrue(!path.select(root).isEmpty());
    }
}
