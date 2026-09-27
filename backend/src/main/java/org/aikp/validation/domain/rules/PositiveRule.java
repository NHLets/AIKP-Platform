package org.aikp.validation.domain.rules;

import org.aikp.validation.domain.*;

public class PositiveRule implements ValidationRule{

    public ValidationResult validate(String value){
        double v = Double.parseDouble(value);
        boolean ok = v>0;
        return new ValidationResult(ok,"Must be positive",
            ok?ValidationSeverity.LOW:ValidationSeverity.HIGH);
    }

    public String code(){ return "POSITIVE"; }
}
