package com.webfuzzing.overlayjvm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests here are based on the data files taken from:
 * <p>
 * https://github.com/IBM/oas-overlay-java
 * <p>
 * in February 2026.
 * These tests rely on specs 1.0.0.
 * All new tests should not come here, and rather use latest version of Overlay specs
 * <p>
 * https://learn.openapis.org/upgrading/overlay-v1.0-to-v1.1.html
 */
public class OverlayJVM_V1_0_0_oas_overlay_java_Test extends ProcessorTestBase {

    @Test
    public void testNotOverlay() {
        assertThrows(
                IllegalArgumentException.class,
                () -> OverlayJVM.parseOverlay(new File("src/test/resources/oas-overlay-java/overlays/not-overlay.yaml"))
        );
    }

    @Test
    public void testInvalidAction() {
        assertThrows(
                IllegalArgumentException.class,
                () -> OverlayJVM.parseOverlay(new File("src/test/resources/oas-overlay-java/overlays/not-jsonpath.yaml"))
        );
    }

    /*
        Following tests are copied&pasted&adapted from:
        https://github.com/IBM/oas-overlay-java
     */


    public static Stream<Data> overlayProvider() {

        return Stream.of(
                new Data("/openapi/town.yaml", "/overlays/remove-properties.yaml", "/expected/town-remove-properties.yaml"),
                new Data("/openapi/petstore.yaml", "/overlays/overlay.yaml", "/expected/output1.yaml"),
                new Data("/openapi/town.yaml", "/overlays/building-description.yaml", "/expected/town-building-description.yaml"),
                new Data("/openapi/town.yaml", "/overlays/update-root.yaml", "/expected/town-root-updated.yaml"),
                new Data("/openapi/town.yaml", "/overlays/remove-example.yaml", "/expected/town-remove-example.yaml"),
                new Data("/openapi/town.yaml", "/overlays/remove-descriptions.yaml", "/expected/town-remove-descriptions.yaml"),
                new Data("/openapi/openapi-with-servers.yaml", "/overlays/remove-server.yaml", "/expected/one-less-server.yaml"),
                //this seems was wrong... you are allowed to modify info.version
                new Data("/openapi/immutable.yaml", "/overlays/immutable.yaml", "/expected/immutable.yaml"),
                new Data("/openapi/responses.yaml", "/overlays/remove-responses.yaml", "/expected/remove-responses.yaml"),
                //this seems was wrong... invalid RFC 9535 path
                new Data("/openapi/traits.yaml", "/overlays/traits.yaml", "/expected/traits.yaml")
        );
    }


    @ParameterizedTest(name = "{0}")
    @MethodSource("overlayProvider")
    public void testOverlay(ProcessorTestBase.Data data) throws Exception {
        verifyOverlay(data, "src/test/resources/oas-overlay-java");
    }

}
