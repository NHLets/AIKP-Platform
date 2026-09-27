package org.aikp.validation.domain.rules;

import org.aikp.validation.domain.*;

public class NonNegativeRule implements ValidationRule{

    public ValidationResult validate(String value){
        double v = Double.parseDouble(value);
        boolean ok = v>=0;
        return new ValidationResult(ok,"Must be non-negative",
            ok?ValidationSeverity.LOW:ValidationSeverity.HIGH);
    }

    public String code(){ return "NON_NEGATIVE"; }
}
