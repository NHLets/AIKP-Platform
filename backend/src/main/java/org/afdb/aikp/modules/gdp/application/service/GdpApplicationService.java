package org.afdb.aikp.modules.gdp.application.service;

import org.afdb.aikp.modules.gdp.domain.repository.GdpReferenceRepository;
import org.afdb.aikp.modules.gdp.domain.repository.GdpReferenceRepository.GdpReferenceRow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class GdpApplicationService {

    private final GdpReferenceRepository repository;

    public GdpApplicationService(GdpReferenceRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<GdpReferenceRow> getGdp(String iso3, Integer year) {
        String normalizedIso3 = iso3 == null || iso3.isBlank()
                ? null
                : iso3.trim().toUpperCase(Locale.ROOT);
        return repository.findGdp(normalizedIso3, year);
    }
}
