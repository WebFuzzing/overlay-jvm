package com.webfuzzing.overlayjvm;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

/**
 * Tests here used the examples defined in the specs definition of Overlay for the new "copy" method
 *
 * https://spec.openapis.org/overlay/latest.html#copy-example
 */
class ProcessorV1_1_0_specs_copyTest extends ProcessorTestBase {


    public static Stream<Data> overlayProvider() {

        return Stream.of(
                new Data("/simple/simple-openapi.yaml", "/simple/simple-overlay.yaml", "/simple/simple-result.yaml"),
                new Data("/exist/exist-openapi.yaml", "/exist/exist-overlay.yaml", "/exist/exist-result.yaml"),
                new Data("/move/move-openapi.yaml", "/move/move-overlay.yaml", "/move/move-result.yaml")
        );
    }


    @ParameterizedTest(name = "{0}")
    @MethodSource("overlayProvider")
    public void testOverlay(ProcessorTestBase.Data data) throws Exception {
        verifyOverlay(data, "src/test/resources/specs-copy");
    }
}
