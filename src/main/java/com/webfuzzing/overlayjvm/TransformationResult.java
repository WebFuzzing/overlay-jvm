package com.webfuzzing.overlayjvm;

import java.util.Collections;
import java.util.List;

public class TransformationResult {

    /**
     * The original OpenAPI schema the transformation was applied on.
     */
    public final String originalSchema;
    /**
     * The original Overlay applied to the input OpenAPI schema.
     */
    public final String appliedOverlay;
    /**
     * The result of the transformation, i.e., when appliedOverlay is applied to the originalSchema.
     */
    public final String transformedSchema;
    /**
     * Possible warning messages when applying the transformations.
     * For example, actions with RFC 9535 json paths that result in no node selection are marked as warning,
     * although technically they are non-erroneous (according to the Overlay specs).
     * This can be useful when debugging json paths, instead of silently ignoring actions that led to no modifications.
     */
    public final List<String> warnings;

    public TransformationResult(String originalSchema, String appliedOverlay, String transformedSchema, List<String> warnings) {
        this.originalSchema = originalSchema;
        this.appliedOverlay = appliedOverlay;
        this.transformedSchema = transformedSchema;
        if(warnings != null){
            this.warnings = Collections.unmodifiableList(warnings);
        } else {
            this.warnings = Collections.emptyList();
        }
    }

    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }
}
