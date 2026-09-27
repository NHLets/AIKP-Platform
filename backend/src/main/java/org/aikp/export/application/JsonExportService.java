package org.aikp.export.application;

import org.aikp.export.domain.*;

import java.util.List;

public class JsonExportService{

    public ExportSchema export(

            String campaign,

            List<NormalizedObservation> observations){

        return new ExportSchema(

                campaign,

                observations);

    }

}