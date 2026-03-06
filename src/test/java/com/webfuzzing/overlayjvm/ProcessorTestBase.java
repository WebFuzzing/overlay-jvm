package com.webfuzzing.overlayjvm;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcessorTestBase {

    protected void verifyOverlay(Data data, String base) throws Exception {

        String openApi = new String(Files.readAllBytes(Paths.get(base, data.openApi)));
        String overlay = new String(Files.readAllBytes(Paths.get(base, data.overlay)));
        String expectedResult = new String(Files.readAllBytes(Paths.get(base, data.expected)));
        expectedResult = FormatUtils.normalizeYaml(expectedResult);

        String result = Processor.applyOverlay(openApi, overlay);
        assertEquals(expectedResult, result);
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
