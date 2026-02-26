package com.webfuzzing.overlayjvm;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests here are based on the data files taken from:
 *
 * https://github.com/IBM/oas-overlay-java
 *
 * in February 2026.
 * These tests rely on specs 1.0.0.
 * All new tests should not come here, and rather use latest version of Overlay specs
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

    FIXME should check all differences between 1.0.0 and 1.1.0... eg regarding required fields
}
