package com.webfuzzing.overlayjvm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests here are based on the data files taken from:
 *
 * https://github.com/IBM/oas-overlay-java
 *
 * in February 2026.
 * These tests rely on specs 1.0.0.
 * All new tests should not come here, and rather use latest version of Overlay specs
 *
 * https://learn.openapis.org/upgrading/overlay-v1.0-to-v1.1.html
 */
public class ProcessorV1_0_0Test {

    @Test
    public void testNotOverlay(){
        assertThrows(
                IllegalArgumentException.class,
                () -> Processor.parseOverlay(new File("src/test/resources/oas-overlay-java/overlays/not-overlay.yaml"))
        );
    }

    @Test
    public void testInvalidAction(){
        assertThrows(
                IllegalArgumentException.class,
                () -> Processor.parseOverlay(new File("src/test/resources/oas-overlay-java/overlays/not-jsonpath.yaml"))
        );
    }

    /*
        Following tests are copied&pasted&adapted from:
        https://github.com/IBM/oas-overlay-java
     */


    public static Stream<Data> overlayProvider() {

        return Stream.of(
                // Commented out as JSON Path doesn't parse path correctly
                // new Data("/openapi/town.yaml", "/overlays/remove-properties.yaml", "/expected/town-remove-properties.yaml"),
                new Data("/openapi/petstore.yaml", "/overlays/overlay.yaml", "/expected/output1.yaml"),
                new Data("/openapi/town.yaml", "/overlays/building-description.yaml", "/expected/town-building-description.yaml"),
                new Data("/openapi/town.yaml", "/overlays/update-root.yaml", "/expected/town-root-updated.yaml"),
                new Data("/openapi/town.yaml", "/overlays/remove-example.yaml", "/expected/town-remove-example.yaml"),
                new Data("/openapi/town.yaml", "/overlays/remove-descriptions.yaml", "/expected/town-remove-descriptions.yaml"),
                new Data("/openapi/openapi-with-servers.yaml", "/overlays/remove-server.yaml", "/expected/one-less-server.yaml"),
                new Data("/openapi/immutable.yaml", "/overlays/immutable.yaml", "/expected/immutable.yaml"),
                new Data("/openapi/responses.yaml", "/overlays/remove-responses.yaml", "/expected/remove-responses.yaml")
                //Commented out as JSON Path doesn't parse and find as expected
                //new Data("/openapi/traits.yaml", "/overlays/traits.yaml", "/expected/traits.yaml"},
        );
    }


    @ParameterizedTest
    @MethodSource("overlayProvider")
    public void testOverlay(Data data) {

        String base = "src/test/resources/oas-overlay-java";

        try {
            String openApi = new String(Files.readAllBytes(Paths.get(base,data.openApi)));
            String overlay = new String(Files.readAllBytes(Paths.get(base,data.overlay)));
            String expectedResult = new String(Files.readAllBytes(Paths.get(base,data.expected)));
            String result = Processor.applyOverlay(openApi, overlay);
            assertEquals(result, expectedResult);
        } catch (IOException e) {
            fail(e.getMessage(), e);
        }
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
    }

}
