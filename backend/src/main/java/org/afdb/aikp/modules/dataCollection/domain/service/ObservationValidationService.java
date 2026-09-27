package org.afdb.aikp.modules.dataCollection.domain.service;

import org.springframework.stereotype.Service;
import org.afdb.aikp.modules.dataCollection.application.dto.ValidationResultDto;

@Service
public class ObservationValidationService {

    public ValidationResultDto validateRequired(String value){

        if(value == null || value.isBlank()){
            return new ValidationResultDto(
                false,
                "REQUIRED",
                "Value is mandatory"
            );
        }

        return new ValidationResultDto(true,"REQUIRED","OK");
    }

    public ValidationResultDto validateNumeric(String value){

        try{
            Double.parseDouble(value);
            return new ValidationResultDto(true,"NUMERIC","OK");
        }catch(Exception ex){
            return new ValidationResultDto(
                false,
                "NUMERIC",
                "Invalid numeric value"
            );
        }
    }

    public ValidationResultDto validateNonNegative(String value){

        try{

            double number = Double.parseDouble(value);

            if(number < 0){
                return new ValidationResultDto(
                    false,
                    "NON_NEGATIVE",
                    "Negative values are not allowed"
                );
            }

            return new ValidationResultDto(true,"NON_NEGATIVE","OK");

        }catch(Exception ex){

            return new ValidationResultDto(
                false,
                "NON_NEGATIVE",
                "Invalid numeric value"
            );

        }
    }

    public ValidationResultDto validatePercentage(String value){

        try{

            double number = Double.parseDouble(value);

            if(number < 0 || number > 100){
                return new ValidationResultDto(
                    false,
                    "PERCENTAGE",
                    "Percentage must be between 0 and 100"
                );
            }

            return new ValidationResultDto(true,"PERCENTAGE","OK");

        }catch(Exception ex){

            return new ValidationResultDto(
                false,
                "PERCENTAGE",
                "Invalid percentage"
            );

        }
    }

}
