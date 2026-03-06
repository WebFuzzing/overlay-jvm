package com.webfuzzing.overlayjvm;

import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcessorTestBase {

    protected void verifyOverlay(Data data, String base) throws Exception {

        String openApi = new String(Files.readAllBytes(Paths.get(base, data.openApi)));
        String overlay = new String(Files.readAllBytes(Paths.get(base, data.overlay)));
        String expectedResult = new String(Files.readAllBytes(Paths.get(base, data.expected)));
        expectedResult = FormatUtils.normalizeJson(expectedResult);

        String result = Processor.applyOverlay(openApi, overlay);
        result = FormatUtils.normalizeJson(result);


        //this was failing on field name order :(
        //assertEquals(expectedResult, result);
        JSONAssert.assertEquals(expectedResult, result, JSONCompareMode.LENIENT);
    }

    public static class Data {
        public final String openApi;
        public final String overlay;
        public final String expected;

        public Data(String openApi, String overlay, String expected) {
            this.openApi = openApi;
            this.overlay = overlay;
            this.expected = expected;
        }

        @Override
        public String toString() {
            return "{" +
                    "overlay='" + overlay + '\'' +
                    ", openApi='" + openApi + '\'' +
                    ", expected='" + expected + '\'' +
                    '}';
        }
    }
}
