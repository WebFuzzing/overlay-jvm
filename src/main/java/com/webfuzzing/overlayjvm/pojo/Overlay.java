package com.webfuzzing.overlayjvm.pojo;

/**
 * https://spec.openapis.org/overlay/latest.html#fixed-fields
 */
public class Overlay {

    /**
     * REQUIRED.
     * This string MUST be the version number of the Overlay Specification that the Overlay document uses.
     * The overlay field SHOULD be used by tooling to interpret the Overlay document.
     */
    public String overlay;

    /**
     * REQUIRED.
     * Provides metadata about the Overlay. The metadata MAY be used by tooling as required.
     */
    public Info info;

    /**
     * URI reference that identifies the target document (such as an [OpenAPI] document) this overlay applies to.
     *
     * NOTE: "extends" is a protected keyword in Java
     */
    @JsonField("extends")
    public String extends_;

    /**
     * REQUIRED.
     * An ordered list of actions to be applied to the target document. The array MUST contain at least one value.
     */
    public List<Action> actions;

}
