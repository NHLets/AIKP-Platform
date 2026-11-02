package org.afdb.aikp.modules.gdp.application.service;

import org.afdb.aikp.modules.gdp.domain.repository.GdpReferenceRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GdpApplicationServiceTest {

    @Test
    void normalizesIso3BeforeQueryingRepository() {
        GdpReferenceRepository repository = mock(GdpReferenceRepository.class);
        when(repository.findGdp("CMR", 2024)).thenReturn(List.of());

        GdpApplicationService service = new GdpApplicationService(repository);
        assertEquals(List.of(), service.getGdp(" cmr ", 2024));

        verify(repository).findGdp("CMR", 2024);
    }

    @Test
    void treatsBlankIso3AsNoCountryFilter() {
        GdpReferenceRepository repository = mock(GdpReferenceRepository.class);
        when(repository.findGdp(null, null)).thenReturn(List.of());

        GdpApplicationService service = new GdpApplicationService(repository);
        assertEquals(List.of(), service.getGdp("  ", null));

        verify(repository).findGdp(null, null);
    }
}
