package com.webfuzzing.overlayjvm.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * https://spec.openapis.org/overlay/latest.html#fixed-fields-0
 */
public class Info {

    private final String title;
    private final String version;
    private final String description;

    @JsonCreator
    public Info(
            @JsonProperty(value = "title", required = true)
            String title,
            @JsonProperty(value = "version", required = true)
            String version,
            @JsonProperty(value = "description")
            String description
    ) {
        this.description = description;
        this.version = version;
        this.title = title;
    }

    /**
     * REQUIRED.
     * A human readable description of the purpose of the overlay.
     */
    public String getTitle() {
        return title;
    }

    /**
     * REQUIRED.
     * A version identifier for indicating changes to the Overlay document.
     */
    public String getVersion() {
        return version;
    }

    /**
     * A description of the Overlay Document.
     * [CommonMark] syntax MAY be used for rich text representation.
     */
    public String getDescription() {
        return description;
    }
}
