package org.aikp.export.domain;

import java.util.List;

public class ExportMapper{

    public ExportSchema build(

            String campaign,

            List<NormalizedObservation> observations){

        return new ExportSchema(

                campaign,

                observations);

    }

}