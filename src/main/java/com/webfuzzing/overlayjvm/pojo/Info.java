package com.webfuzzing.overlayjvm.pojo;

/**
 * https://spec.openapis.org/overlay/latest.html#fixed-fields-0
 */
public class Info {

    /**
     * REQUIRED.
     * A human readable description of the purpose of the overlay.
     */
    public String title;

    /**
     * REQUIRED.
     * A version identifier for indicating changes to the Overlay document.
     */
    public String version;

    /**
     * A description of the Overlay Document.
     * [CommonMark] syntax MAY be used for rich text representation.
     */
    public String description;
}
