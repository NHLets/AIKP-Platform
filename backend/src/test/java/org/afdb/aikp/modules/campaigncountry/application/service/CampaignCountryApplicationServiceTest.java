package org.afdb.aikp.modules.campaigncountry.application.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.afdb.aikp.modules.campaign.domain.exception.CampaignNotFoundException;
import org.afdb.aikp.modules.campaign.domain.repository.CampaignRepository;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignId;

import org.afdb.aikp.modules.campaigncountry.application.command.AddCountryToCampaignCommand;
import org.afdb.aikp.modules.campaigncountry.application.command.RemoveCountryFromCampaignCommand;
import org.afdb.aikp.modules.campaigncountry.application.mapper.CampaignCountryApplicationMapper;
import org.afdb.aikp.modules.campaigncountry.application.query.GetCampaignCountriesQuery;
import org.afdb.aikp.modules.campaigncountry.application.query.GetCampaignCountryQuery;
import org.afdb.aikp.modules.campaigncountry.application.response.CampaignCountryResponse;

import org.afdb.aikp.modules.campaigncountry.domain.exception.CampaignCountryNotFoundException;
import org.afdb.aikp.modules.campaigncountry.domain.exception.CountryAlreadyAssignedToCampaignException;
import org.afdb.aikp.modules.campaigncountry.domain.model.CampaignCountry;
import org.afdb.aikp.modules.campaigncountry.domain.repository.CampaignCountryRepository;

import org.afdb.aikp.modules.country.domain.exception.CountryNotFoundException;
import org.afdb.aikp.modules.country.domain.repository.CountryRepository;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CampaignCountryApplicationServiceTest {

    private CampaignRepository campaignRepository;

    private CountryRepository countryRepository;

    private CampaignCountryRepository campaignCountryRepository;

    private CampaignCountryApplicationMapper mapper;

    private CampaignCountryApplicationService service;

    @BeforeEach
    void setUp() {

        campaignRepository =
                mock(CampaignRepository.class);

        countryRepository =
                mock(CountryRepository.class);

        campaignCountryRepository =
                mock(CampaignCountryRepository.class);

        mapper =
                new CampaignCountryApplicationMapper();

        service =
                new CampaignCountryApplicationService(
                        campaignRepository,
                        countryRepository,
                        campaignCountryRepository,
                        mapper
                );
    }

    @Test
    void shouldAddCountryToCampaign() {

        UUID campaignUuid = UUID.randomUUID();
        UUID countryUuid = UUID.randomUUID();

        CampaignId campaignId =
                CampaignId.of(campaignUuid);

        CountryId countryId =
                CountryId.of(countryUuid);

        when(campaignRepository.existsById(campaignId))
                .thenReturn(true);

        when(countryRepository.existsById(countryId))
                .thenReturn(true);

        when(campaignCountryRepository
                .existsByCampaignIdAndCountryId(
                        campaignId,
                        countryId))
                .thenReturn(false);

        when(campaignCountryRepository.save(any()))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        CampaignCountryResponse response =
                service.addCountryToCampaign(
                        new AddCountryToCampaignCommand(
                                campaignUuid,
                                countryUuid
                        )
                );

        assertEquals(
                campaignUuid,
                response.campaignId());

        assertEquals(
                countryUuid,
                response.countryId());

        verify(campaignCountryRepository)
                .save(any(CampaignCountry.class));
    }

    @Test
    void shouldThrowWhenCampaignDoesNotExist() {

        UUID campaignUuid = UUID.randomUUID();
        UUID countryUuid = UUID.randomUUID();

        when(campaignRepository.existsById(
                CampaignId.of(campaignUuid)))
                .thenReturn(false);

        assertThrows(
                CampaignNotFoundException.class,
                () -> service.addCountryToCampaign(
                        new AddCountryToCampaignCommand(
                                campaignUuid,
                                countryUuid
                        )
                )
        );

        verify(countryRepository, never())
                .existsById(any());

        verify(campaignCountryRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowWhenCountryDoesNotExist() {

        UUID campaignUuid = UUID.randomUUID();
        UUID countryUuid = UUID.randomUUID();

        when(campaignRepository.existsById(
                CampaignId.of(campaignUuid)))
                .thenReturn(true);

        when(countryRepository.existsById(
                CountryId.of(countryUuid)))
                .thenReturn(false);

        assertThrows(
                CountryNotFoundException.class,
                () -> service.addCountryToCampaign(
                        new AddCountryToCampaignCommand(
                                campaignUuid,
                                countryUuid
                        )
                )
        );

        verify(campaignCountryRepository, never())
                .save(any());
    }

    @Test
    void shouldThrowWhenCountryAlreadyAssignedToCampaign() {

        UUID campaignUuid = UUID.randomUUID();
        UUID countryUuid = UUID.randomUUID();

        CampaignId campaignId =
                CampaignId.of(campaignUuid);

        CountryId countryId =
                CountryId.of(countryUuid);

        when(campaignRepository.existsById(campaignId))
                .thenReturn(true);

        when(countryRepository.existsById(countryId))
                .thenReturn(true);

        when(campaignCountryRepository
                .existsByCampaignIdAndCountryId(
                        campaignId,
                        countryId))
                .thenReturn(true);

        assertThrows(
                CountryAlreadyAssignedToCampaignException.class,
                () -> service.addCountryToCampaign(
                        new AddCountryToCampaignCommand(
                                campaignUuid,
                                countryUuid
                        )
                )
        );

        verify(campaignCountryRepository, never())
                .save(any());
    }

    @Test
    void shouldGetCampaignCountries() {

        UUID campaignUuid = UUID.randomUUID();

        CampaignId campaignId =
                CampaignId.of(campaignUuid);

        UUID countryOneUuid = UUID.randomUUID();
        UUID countryTwoUuid = UUID.randomUUID();

        CampaignCountry countryOne =
                CampaignCountry.create(
                        campaignId,
                        CountryId.of(countryOneUuid)
                );

        CampaignCountry countryTwo =
                CampaignCountry.create(
                        campaignId,
                        CountryId.of(countryTwoUuid)
                );

        when(campaignRepository.existsById(campaignId))
                .thenReturn(true);

        when(campaignCountryRepository
                .findByCampaignId(campaignId))
                .thenReturn(
                        List.of(
                                countryOne,
                                countryTwo
                        )
                );

        List<CampaignCountryResponse> response =
                service.getCampaignCountries(
                        new GetCampaignCountriesQuery(
                                campaignUuid
                        )
                );

        assertEquals(2, response.size());

        assertEquals(
                countryOneUuid,
                response.get(0).countryId());

        assertEquals(
                countryTwoUuid,
                response.get(1).countryId());
    }

    @Test
    void shouldThrowWhenGettingCountriesForUnknownCampaign() {

        UUID campaignUuid = UUID.randomUUID();

        when(campaignRepository.existsById(
                CampaignId.of(campaignUuid)))
                .thenReturn(false);

        assertThrows(
                CampaignNotFoundException.class,
                () -> service.getCampaignCountries(
                        new GetCampaignCountriesQuery(
                                campaignUuid
                        )
                )
        );

        verify(campaignCountryRepository, never())
                .findByCampaignId(any());
    }

    @Test
    void shouldGetCampaignCountry() {

        UUID campaignUuid = UUID.randomUUID();
        UUID countryUuid = UUID.randomUUID();

        CampaignId campaignId =
                CampaignId.of(campaignUuid);

        CountryId countryId =
                CountryId.of(countryUuid);

        CampaignCountry campaignCountry =
                CampaignCountry.create(
                        campaignId,
                        countryId
                );

        when(campaignCountryRepository
                .findByCampaignIdAndCountryId(
                        campaignId,
                        countryId))
                .thenReturn(
                        Optional.of(campaignCountry)
                );

        CampaignCountryResponse response =
                service.getCampaignCountry(
                        new GetCampaignCountryQuery(
                                campaignUuid,
                                countryUuid
                        )
                );

        assertEquals(
                campaignUuid,
                response.campaignId());

        assertEquals(
                countryUuid,
                response.countryId());
    }

    @Test
    void shouldThrowWhenCampaignCountryDoesNotExist() {

        UUID campaignUuid = UUID.randomUUID();
        UUID countryUuid = UUID.randomUUID();

        when(campaignCountryRepository
                .findByCampaignIdAndCountryId(
                        CampaignId.of(campaignUuid),
                        CountryId.of(countryUuid)))
                .thenReturn(Optional.empty());

        assertThrows(
                CampaignCountryNotFoundException.class,
                () -> service.getCampaignCountry(
                        new GetCampaignCountryQuery(
                                campaignUuid,
                                countryUuid
                        )
                )
        );
    }

    @Test
    void shouldRemoveCountryFromCampaign() {

        UUID campaignUuid = UUID.randomUUID();
        UUID countryUuid = UUID.randomUUID();

        CampaignId campaignId =
                CampaignId.of(campaignUuid);

        CountryId countryId =
                CountryId.of(countryUuid);

        CampaignCountry campaignCountry =
                CampaignCountry.create(
                        campaignId,
                        countryId
                );

        when(campaignCountryRepository
                .findByCampaignIdAndCountryId(
                        campaignId,
                        countryId))
                .thenReturn(
                        Optional.of(campaignCountry)
                );

        assertDoesNotThrow(
                () -> service.removeCountryFromCampaign(
                        new RemoveCountryFromCampaignCommand(
                                campaignUuid,
                                countryUuid
                        )
                )
        );

        verify(campaignCountryRepository)
                .delete(campaignCountry);
    }

    @Test
    void shouldThrowWhenRemovingUnknownCampaignCountry() {

        UUID campaignUuid = UUID.randomUUID();
        UUID countryUuid = UUID.randomUUID();

        when(campaignCountryRepository
                .findByCampaignIdAndCountryId(
                        CampaignId.of(campaignUuid),
                        CountryId.of(countryUuid)))
                .thenReturn(Optional.empty());

        assertThrows(
                CampaignCountryNotFoundException.class,
                () -> service.removeCountryFromCampaign(
                        new RemoveCountryFromCampaignCommand(
                                campaignUuid,
                                countryUuid
                        )
                )
        );

        verify(campaignCountryRepository, never())
                .delete(any());
    }
}
