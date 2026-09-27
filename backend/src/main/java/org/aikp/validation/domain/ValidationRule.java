package org.aikp.validation.domain;

public interface ValidationRule{

    ValidationResult validate(String value);

    String code();

}