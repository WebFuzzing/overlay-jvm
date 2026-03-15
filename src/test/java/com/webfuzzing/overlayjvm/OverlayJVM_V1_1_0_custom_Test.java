package com.webfuzzing.overlayjvm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.noear.snack4.ONode;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class OverlayJVM_V1_1_0_custom_Test extends ProcessorTestBase {

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

    @Test
    public void testLibrarySupport() throws Exception {

        String json = readResource("custom/array/array.json");
        assertNotNull(json);

        ONode schema = ONode.ofJson(json);

        //----------------------------------------------------
        //this should return no results
        String q0 = "$.a[?@.y]";
        boolean e0 = schema.exists(q0);
        ONode r0 = schema.select(q0);

        //assertFalse(e0); //this fails
        assertTrue(r0.isArray());
        assertEquals(0, r0.size());
        assertNull(r0.parent());

        //----------------------------------------------------
        //this should return the empty array in b
        String q1 = "$.b";
        boolean e1 = schema.exists(q1);
        ONode r1 = schema.select(q1);

        assertTrue(e1);
        assertTrue(r1.isArray());
        assertEquals(0, r1.size());
        //is checking the parent the only way to see if the target is an array?
        assertNotNull(r1.parent());

        //----------------------------------------------------
        //should get the elements in c and d
        String q2 = "$.*.y";
        boolean e2 = schema.exists(q2);
        ONode r2 = schema.select(q2);

        assertTrue(e2);
        assertTrue(r2.isArray());
        assertEquals(2, r2.size());
        assertNull(r2.parent());
        // the returned nodes in the result arrays are not copies, but references to original tree, sharing same root
        assertEquals(r2.get(0).parent().parent(), r2.get(1).parent().parent());

        //----------------------------------------------------
        // although e is null, it exists
        String q3 = "$.e";
        boolean e3 = schema.exists(q3);
        ONode r3 = schema.select(q3);

        assertTrue(e3);
        assertFalse(r3.isArray());
        assertTrue(r3.isNull());
        assertNotNull(r3.parent());

        //----------------------------------------------------
        // f is undefined in the document
        String q4 = "$.f";
        boolean e4 = schema.exists(q4);
        ONode r4 = schema.select(q4);

        //if undefined, then it is treated as a "null" node, but with no parent, and with "exists" returning false
        assertFalse(e4);
        assertFalse(r4.isArray());
        assertTrue(r4.isNull());
        assertNull(r4.parent());

        //----------------------------------------------------
        // f is undefined
        String q5 = "$.f[?@.y]";
        boolean e5 = schema.exists(q5);
        ONode r5 = schema.select(q5);

        //assertFalse(e5); // this fails???
        assertTrue(r5.isArray()); //this is now treated as an empty array?
        assertEquals(0, r5.size());
        assertNull(r5.parent());
    }
}
