package org.aikp.validation.domain;

import java.util.List;

public class ValidationEngine{

    private final List<ValidationRule> rules;

    public ValidationEngine(List<ValidationRule> rules){
        this.rules = rules;
    }

    public List<ValidationResult> validate(String value){
        return rules.stream()
                .map(rule->rule.validate(value))
                .toList();
    }

}