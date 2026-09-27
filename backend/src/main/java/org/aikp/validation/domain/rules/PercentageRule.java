package org.aikp.validation.domain.rules;

import org.aikp.validation.domain.*;

public class PercentageRule implements ValidationRule{

    public ValidationResult validate(String value){
        double v = Double.parseDouble(value);
        boolean ok = v>=0 && v<=100;
        return new ValidationResult(ok,"Percentage range",
            ok?ValidationSeverity.LOW:ValidationSeverity.MEDIUM);
    }

    public String code(){ return "PERCENTAGE"; }
}
