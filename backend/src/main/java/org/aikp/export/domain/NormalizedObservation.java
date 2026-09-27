package org.aikp.export.domain;

public record NormalizedObservation(

    String countryIso3,

    String countryName,

    String organizationCode,

    String organizationName,

    String questionnaire,

    String indicatorCode,

    String indicatorLabel,

    Double value,

    String unit,

    Integer referenceYear,

    String status

){}