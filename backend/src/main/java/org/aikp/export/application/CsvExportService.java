package org.aikp.export.application;

import org.aikp.export.domain.*;

import java.util.List;

public class CsvExportService{

    public String export(
            List<NormalizedObservation> observations){

        String header = String.join(",",
            "country_iso3",
            "country_name",
            "organization_code",
            "organization_name",
            "questionnaire",
            "indicator_code",
            "indicator_label",
            "value",
            "unit",
            "reference_year",
            "status"
        );

        return header;
    }

}