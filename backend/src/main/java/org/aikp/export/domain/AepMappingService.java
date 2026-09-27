package org.aikp.export.domain;

import java.util.List;

public class AepMappingService{

    public List<AepIndicatorMapping> defaultMappings(){

        return List.of(

            new AepIndicatorMapping(
                "PWC_145",
                "ENERGY_SOLD",
                "GWh"
            ),

            new AepIndicatorMapping(
                "PWC_152",
                "NETWORK_LOSSES",
                "GWh"
            ),

            new AepIndicatorMapping(
                "PWC_153",
                "LOSS_RATE",
                "%"
            ),

            new AepIndicatorMapping(
                "PWB_084",
                "INSTALLED_CAPACITY",
                "MW"
            ),

            new AepIndicatorMapping(
                "PWB_102",
                "ELECTRICITY_GENERATION",
                "GWh"
            )

        );

    }

}