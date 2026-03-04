package com.webfuzzing.overlayjvm.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

/**
 * https://spec.openapis.org/overlay/latest.html#fixed-fields
 */
public class Overlay {

    private final String overlay;
    private final Info info;
    private final String extends_;
    private final List<Action> actions;

    @JsonCreator
    public Overlay(
            @JsonProperty(value = "overlay", required = true)
            String overlay,
            @JsonProperty(value = "info", required = true)
            Info info,
            @JsonProperty("extends")
            String extends_,
            @JsonProperty(value = "actions", required = true)
            List<Action> actions) {

        if(actions == null || actions.isEmpty()){
            throw new IllegalArgumentException("List 'actions' cannot be empty");
        }

        this.overlay = overlay;
        this.info = info;
        this.extends_ = extends_;
        this.actions = Collections.unmodifiableList(actions);
    }

    /**
     * REQUIRED.
     * This string MUST be the version number of the Overlay Specification that the Overlay document uses.
     * The overlay field SHOULD be used by tooling to interpret the Overlay document.
     */
    public String getOverlay() {
        return overlay;
    }

    /**
     * REQUIRED.
     * Provides metadata about the Overlay. The metadata MAY be used by tooling as required.
     */
    public Info getInfo() {
        return info;
    }

    /**
     * URI reference that identifies the target document (such as an [OpenAPI] document) this overlay applies to.
     *
     * NOTE: "extends" is a protected keyword in Java
     */
    public String getExtends() {
        return extends_;
    }

    /**
     * REQUIRED.
     * An ordered list of actions to be applied to the target document. The array MUST contain at least one value.
     */
    public List<Action> getActions() {
        return actions;
    }
}
