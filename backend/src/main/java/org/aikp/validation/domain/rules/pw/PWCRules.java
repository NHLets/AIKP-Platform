package org.aikp.validation.domain.rules.pw;

import org.aikp.validation.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PWCRules{

    public List<ValidationResult> validate(Map<String,Double> values){

        List<ValidationResult> results = new ArrayList<>();

        checkCustomerTotals(values,results);
        checkEnergySold(values,results);
        checkNetworkLoss(values,results);
        checkLossRate(values,results);

        return results;
    }

    private void checkCustomerTotals(
        Map<String,Double> v,
        List<ValidationResult> r){

        double total = get(v,"TOTAL_CUSTOMERS");
        double sum =
            get(v,"RESIDENTIAL_CUSTOMERS")
          + get(v,"COMMERCIAL_CUSTOMERS")
          + get(v,"INDUSTRIAL_CUSTOMERS")
          + get(v,"OTHER_CUSTOMERS");

        r.add(new ValidationResult(
            total==sum,
            "Customer totals consistency",
            ValidationSeverity.HIGH
        ));
    }

    private void checkEnergySold(
        Map<String,Double> v,
        List<ValidationResult> r){

        double total = get(v,"ENERGY_SOLD_TOTAL");
        double sum =
            get(v,"ENERGY_SOLD_RESIDENTIAL")
          + get(v,"ENERGY_SOLD_COMMERCIAL")
          + get(v,"ENERGY_SOLD_INDUSTRIAL")
          + get(v,"ENERGY_SOLD_OTHER");

        r.add(new ValidationResult(
            total==sum,
            "Energy sold consistency",
            ValidationSeverity.HIGH
        ));
    }

    private void checkNetworkLoss(
        Map<String,Double> v,
        List<ValidationResult> r){

        double distributed = get(v,"ENERGY_DISTRIBUTED");
        double sold = get(v,"ENERGY_SOLD_TOTAL");
        double losses = get(v,"NETWORK_LOSSES");

        r.add(new ValidationResult(
            (distributed-sold)==losses,
            "Network losses consistency",
            ValidationSeverity.HIGH
        ));
    }

    private void checkLossRate(
        Map<String,Double> v,
        List<ValidationResult> r){

        double distributed = get(v,"ENERGY_DISTRIBUTED");
        double losses = get(v,"NETWORK_LOSSES");
        double rate = get(v,"LOSS_RATE");

        if(distributed==0){
            return;
        }

        double calc = losses/distributed*100;

        r.add(new ValidationResult(
            Math.abs(calc-rate)<0.01,
            "Loss rate consistency",
            ValidationSeverity.MEDIUM
        ));
    }

    private double get(Map<String,Double> v,String key){
        return v.getOrDefault(key,0.0);
    }

}
