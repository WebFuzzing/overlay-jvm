package com.webfuzzing.overlayjvm;

import java.util.Collections;
import java.util.List;

public class TransformationResult {

    public final String originalSchema;
    public final String appliedOverlay;
    public final String transformedSchema;
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
