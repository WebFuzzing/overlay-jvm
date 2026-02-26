package com.webfuzzing.overlayjvm.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * https://spec.openapis.org/overlay/latest.html#fixed-fields-1
 */
public class Action {

    private final String target;
    private final String description;
    private final JsonNode update;
    private final String copy;
    private final Boolean remove;

    @JsonCreator
    public Action(
            @JsonProperty(value = "target", required = true)
            String target,
            @JsonProperty("description")
            String description,
            @JsonProperty("update")
            JsonNode update,
            @JsonProperty("copy")
            String copy,
            @JsonProperty("remove")
            Boolean remove
    ) {
        this.target = target;
        this.description = description;
        this.update = update;
        this.copy = copy;
        this.remove = remove;
    }

    /**
     * REQUIRED
     * A RFC9535 JSONPath query expression selecting nodes in the target document.
     */
    public String getTarget() {
        return target;
    }

    /**
     * A description of the action.
     * [CommonMark] syntax MAY be used for rich text representation.
     */
    public String getDescription() {
        return description;
    }

    /**
     * If the target selects object nodes, the value of this field MUST be an object with the properties
     * and values to merge with each selected object.
     * If the target selects array nodes, the value of this field MUST be an array to concatenate
     * with each selected array, or an object or primitive value to append to each selected array.
     * If the target selects primitive nodes, the value of this field MUST be a primitive value to replace
     * each selected node.
     * This field has no impact if the remove field of this action object is true or if the copy field contains a value.
     */
    public JsonNode getUpdate() {
        return update;
    }

    /**
     * A JSONPath expression selecting a single node to copy into the target nodes.
     * If the target selects object nodes, the value of this field MUST be an object with the properties
     * and values to merge with each selected object.
     * If the target selects array nodes, the value of this field MUST be an array to concatenate with
     * each selected array, or an object or primitive value to append to each selected array.
     * If the target selects primitive nodes, the value of this field MUST be a primitive value to replace
     * each selected node.
     * This field has no impact if the remove field of this action object is true or if the update field contains a value.
     */
    public String getCopy() {
        return copy;
    }

    /**
     * A boolean value that indicates that each of the target nodes MUST be removed from the the map
     * or array it is contained in.
     * The default value is false.
     */
    public Boolean getRemove() {
        return remove;
    }
}
