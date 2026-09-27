package org.aikp.validation.domain.rules.pw;

import org.aikp.validation.domain.*;

public class TemporalValidationRule {

    public ValidationResult validate(
            String variableCode,
            double previousValue,
            double currentValue) {

        if(previousValue==0){
            return new ValidationResult(
                    true,
                    variableCode + ": no previous value",
                    ValidationSeverity.LOW);
        }

        double change =
                Math.abs(currentValue-previousValue)
                / previousValue * 100;

        ValidationSeverity severity =
                change>100 ? ValidationSeverity.HIGH :
                change>50  ? ValidationSeverity.MEDIUM :
                change>20  ? ValidationSeverity.LOW :
                             ValidationSeverity.LOW;

        boolean valid = change<=100;

        return new ValidationResult(
                valid,
                variableCode + ": " + Math.round(change) + "% change",
                severity);
    }

}