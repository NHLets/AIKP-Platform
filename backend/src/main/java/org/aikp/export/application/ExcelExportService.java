package org.aikp.export.application;

import org.aikp.export.domain.*;

import java.util.List;

public class ExcelExportService{

    public String exportPW_A(
            List<NormalizedObservation> observations){

        return "PW_A.xlsx";
    }

    public String exportPW_B(
            List<NormalizedObservation> observations){

        return "PW_B.xlsx";
    }

    public String exportPW_C(
            List<NormalizedObservation> observations){

        return "PW_C.xlsx";
    }

    public String exportFG(
            List<NormalizedObservation> observations){

        return "F_G.xlsx";
    }

}