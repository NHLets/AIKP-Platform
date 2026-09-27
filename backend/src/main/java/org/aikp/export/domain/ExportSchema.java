package org.aikp.export.domain;

import java.util.List;

public record ExportSchema(

    String campaign,

    List<NormalizedObservation> observations

){}