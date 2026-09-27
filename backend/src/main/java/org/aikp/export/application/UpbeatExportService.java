package org.aikp.export.application;

import org.aikp.export.domain.*;

import java.util.List;

public class UpbeatExportService{

    public String export(
            List<NormalizedObservation> observations){

        String header = String.join(",",
            "country_iso3",
            "organization_code",
            "indicator_code",
            "indicator_value",
            "unit",
            "reference_year",
            "source",
            "validated"
        );

        return header;
    }

}