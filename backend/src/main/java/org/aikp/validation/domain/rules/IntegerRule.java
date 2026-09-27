package org.aikp.validation.domain.rules;

import org.aikp.validation.domain.*;

public class IntegerRule implements ValidationRule{

    public ValidationResult validate(String value){
        boolean ok = value.matches("^-?\\d+$");
        return new ValidationResult(ok,"Integer required",
            ok?ValidationSeverity.LOW:ValidationSeverity.MEDIUM);
    }

    public String code(){ return "INTEGER"; }
}
