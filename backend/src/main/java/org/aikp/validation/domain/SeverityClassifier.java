package org.aikp.validation.domain;

import java.util.List;

public class SeverityClassifier {

    public ValidationSeverity classify(
            List<ValidationResult> results){

        if(results.stream().anyMatch(r->
                r.severity()==ValidationSeverity.HIGH)){
            return ValidationSeverity.HIGH;
        }

        if(results.stream().anyMatch(r->
                r.severity()==ValidationSeverity.MEDIUM)){
            return ValidationSeverity.MEDIUM;
        }

        return ValidationSeverity.LOW;
    }

}