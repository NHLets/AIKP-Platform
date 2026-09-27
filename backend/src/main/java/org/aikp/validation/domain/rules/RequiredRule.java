package org.aikp.validation.domain.rules;

import org.aikp.validation.domain.*;

public class RequiredRule implements ValidationRule{

    public ValidationResult validate(String value){
        boolean ok = value!=null && !value.isBlank();
        return new ValidationResult(ok,"Required field",
            ok?ValidationSeverity.LOW:ValidationSeverity.HIGH);
    }

    public String code(){ return "REQUIRED"; }
}
