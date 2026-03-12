package com.webfuzzing.overlayjvm;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

//FIXME: https://github.com/noear/snackjson/issues/56
@Disabled
public class ProcessorV1_1_0_customTest  extends ProcessorTestBase {

    public static Stream<Data> overlayProvider() {

        return Stream.of(
                getDataFromName("query-examples")
        );
    }


    @ParameterizedTest(name = "{0}")
    @MethodSource("overlayProvider")
    public void testOverlay(ProcessorTestBase.Data data) throws Exception {
        verifyOverlay(data, "src/test/resources/custom");
    }
}
