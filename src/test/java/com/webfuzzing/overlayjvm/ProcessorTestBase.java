package com.webfuzzing.overlayjvm;

import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class ProcessorTestBase {

    protected String readResource(String resource) throws Exception {
        Path path = Paths.get(this.getClass().getClassLoader().getResource(resource).toURI());
        return new String(Files.readAllBytes(path));
    }

    protected void verifyOverlay(Data data, String base) throws Exception {

        String openApi = new String(Files.readAllBytes(Paths.get(base, data.openApi)));
        String overlay = new String(Files.readAllBytes(Paths.get(base, data.overlay)));
        String expectedResult = new String(Files.readAllBytes(Paths.get(base, data.expected)));
        expectedResult = FormatUtils.normalizeJson(expectedResult);

        TransformationResult tr = OverlayJVM.applyOverlay(openApi, overlay);
        String result = FormatUtils.normalizeJson(tr.transformedSchema);

        assertEquals(data.warningsCount, tr.warnings.size(), "Expected " + data.warningsCount + "warning:\n"+String.join("\n", tr.warnings));

        //this was failing on field name order :(
        //assertEquals(expectedResult, result);
        JSONAssert.assertEquals(expectedResult, result, JSONCompareMode.LENIENT);
    }

    public static class Data {
        public final String openApi;
        public final String overlay;
        public final String expected;
        public final int warningsCount;

        public Data(String openApi, String overlay, String expected, int warningsCount) {
            this.openApi = openApi;
            this.overlay = overlay;
            this.expected = expected;
            this.warningsCount = warningsCount;
        }

        @Override
        public String toString() {
            return "{" +
                    "overlay='" + overlay + '\'' +
                    ", openApi='" + openApi + '\'' +
                    ", expected='" + expected + '\'' +
                    ", warningsCount=" + warningsCount +
                    '}';
        }
    }

    public static Data getDataFromName(String name, int warningsCount) {
        return new Data(
                "/"+name+"/"+name+"-openapi.yaml",
                "/"+name+"/"+name+"-overlay.yaml",
                "/"+name+"/"+name+"-result.yaml",
                warningsCount
        );
    }
}
