package org.afdb.aikp.modules.gdp.domain.repository;

import java.math.BigDecimal;
import java.util.List;

/** Read-only port for annual nominal GDP reference data. */
public interface GdpReferenceRepository {
    List<GdpReferenceRow> findGdp(String iso3Code, Integer year);

    record GdpReferenceRow(
            String iso3Code,
            String countryName,
            int year,
            BigDecimal gdpCurrentUsd,
            String sourceName,
            String sourceRevision,
            String sourceSeriesCode,
            String sourceUrl) {
    }
}
