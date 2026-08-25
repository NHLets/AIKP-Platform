package org.afdb.aikp.modules.collection.infrastructure.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.afdb.aikp.modules.campaign.domain.model.Campaign;
import org.afdb.aikp.modules.campaign.domain.repository.CampaignRepository;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignCode;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignDescription;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignName;

import org.afdb.aikp.modules.collection.domain.model.DataCollection;
import org.afdb.aikp.modules.collection.domain.repository.DataCollectionRepository;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;

import org.afdb.aikp.modules.country.domain.model.Country;
import org.afdb.aikp.modules.country.domain.repository.CountryRepository;
import org.afdb.aikp.modules.country.domain.valueobject.CountryId;
import org.afdb.aikp.modules.country.domain.valueobject.CountryName;
import org.afdb.aikp.modules.country.domain.valueobject.Iso2Code;
import org.afdb.aikp.modules.country.domain.valueobject.Iso3Code;
import org.afdb.aikp.modules.country.domain.valueobject.NumericCode;
import org.afdb.aikp.modules.country.domain.valueobject.OfficialCountryName;

import org.afdb.aikp.modules.organization.domain.enums.OrganizationType;
import org.afdb.aikp.modules.organization.domain.model.Organization;
import org.afdb.aikp.modules.organization.domain.repository.OrganizationRepository;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationCode;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationName;

import org.afdb.aikp.modules.person.domain.model.Person;
import org.afdb.aikp.modules.person.domain.repository.PersonRepository;
import org.afdb.aikp.modules.person.domain.valueobject.PersonFullName;
import org.afdb.aikp.modules.person.domain.valueobject.PersonId;

import org.afdb.aikp.modules.questionnaire.domain.enums.RenderType;
import org.afdb.aikp.modules.questionnaire.domain.model.Questionnaire;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireRepository;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.DefaultLanguage;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireCode;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireDescription;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireName;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVersion;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class DataCollectionRepositoryAdapterIntegrationTest {

    private static final AtomicInteger
            COUNTRY_SEQUENCE =
                    new AtomicInteger(20000);

    @Autowired
    private DataCollectionRepository repository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private PersonRepository personRepository;

    @Test
    void shouldSaveAndFindDataCollectionById() {

        DataCollection dataCollection =
                createDataCollection();

        DataCollection saved =
                repository.save(dataCollection);

        var result =
                repository.findById(
                        saved.getDataCollectionId());

        assertThat(result).isPresent();

        DataCollection found =
                result.orElseThrow();

        assertThat(found.getDataCollectionId())
                .isEqualTo(
                        saved.getDataCollectionId());

        assertThat(found.getCampaignId())
                .isEqualTo(
                        saved.getCampaignId());

        assertThat(found.getCountryId())
                .isEqualTo(
                        saved.getCountryId());

        assertThat(found.getQuestionnaireId())
                .isEqualTo(
                        saved.getQuestionnaireId());

        assertThat(found.getResponsibleOrganizationId())
                .isEqualTo(
                        saved.getResponsibleOrganizationId());

        assertThat(found.getDataCollectorId())
                .isEqualTo(
                        saved.getDataCollectorId());

        assertThat(found.getStatus())
                .isEqualTo(
                        saved.getStatus());
    }

    @Test
    void shouldFindAllDataCollections() {

        DataCollection first =
                repository.save(
                        createDataCollection());

        DataCollection second =
                repository.save(
                        createDataCollection());

        List<DataCollection> results =
                repository.findAll();

        assertThat(results)
                .extracting(
                        DataCollection::getDataCollectionId)
                .contains(
                        first.getDataCollectionId(),
                        second.getDataCollectionId());
    }

    @Test
    void shouldFindDataCollectionsByCampaignId() {

        Campaign campaign =
                createAndSaveCampaign();

        DataCollection first =
                repository.save(
                        createDataCollection(
                                campaign));

        DataCollection second =
                repository.save(
                        createDataCollection(
                                campaign));

        repository.save(
                createDataCollection());

        List<DataCollection> results =
                repository.findByCampaignId(
                        campaign.getId());

        assertThat(results)
                .extracting(
                        DataCollection::getDataCollectionId)
                .containsExactlyInAnyOrder(
                        first.getDataCollectionId(),
                        second.getDataCollectionId());
    }

    @Test
    void shouldFindDataCollectionsByCountryId() {

        Country country =
                createAndSaveCountry();

        DataCollection first =
                repository.save(
                        createDataCollection(
                                country));

        DataCollection second =
                repository.save(
                        createDataCollection(
                                country));

        repository.save(
                createDataCollection());

        List<DataCollection> results =
                repository.findByCountryId(
                        country.getCountryId());

        assertThat(results)
                .extracting(
                        DataCollection::getDataCollectionId)
                .containsExactlyInAnyOrder(
                        first.getDataCollectionId(),
                        second.getDataCollectionId());
    }

    @Test
    void shouldReturnTrueWhenDataCollectionExists() {

        DataCollection saved =
                repository.save(
                        createDataCollection());

        boolean exists =
                repository.existsById(
                        saved.getDataCollectionId());

        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenDataCollectionDoesNotExist() {

        boolean exists =
                repository.existsById(
                        DataCollectionId.generate());

        assertThat(exists).isFalse();
    }

    @Test
    void shouldDeleteDataCollection() {

        DataCollection saved =
                repository.save(
                        createDataCollection());

        repository.delete(saved);

        boolean exists =
                repository.existsById(
                        saved.getDataCollectionId());

        assertThat(exists).isFalse();
    }

    private DataCollection createDataCollection() {

        Campaign campaign =
                createAndSaveCampaign();

        Country country =
                createAndSaveCountry();

        Questionnaire questionnaire =
                createAndSaveQuestionnaire();

        Organization organization =
                createAndSaveOrganization(
                        country);

        Person person =
                createAndSavePerson(
                        organization);

        return DataCollection.create(
                DataCollectionId.generate(),
                campaign.getId(),
                country.getCountryId(),
                questionnaire.getId(),
                organization.getOrganizationId(),
                person.getPersonId());
    }

    private DataCollection createDataCollection(
            Campaign campaign) {

        Country country =
                createAndSaveCountry();

        Questionnaire questionnaire =
                createAndSaveQuestionnaire();

        Organization organization =
                createAndSaveOrganization(
                        country);

        Person person =
                createAndSavePerson(
                        organization);

        return DataCollection.create(
                DataCollectionId.generate(),
                campaign.getId(),
                country.getCountryId(),
                questionnaire.getId(),
                organization.getOrganizationId(),
                person.getPersonId());
    }

    private DataCollection createDataCollection(
            Country country) {

        Campaign campaign =
                createAndSaveCampaign();

        Questionnaire questionnaire =
                createAndSaveQuestionnaire();

        Organization organization =
                createAndSaveOrganization(
                        country);

        Person person =
                createAndSavePerson(
                        organization);

        return DataCollection.create(
                DataCollectionId.generate(),
                campaign.getId(),
                country.getCountryId(),
                questionnaire.getId(),
                organization.getOrganizationId(),
                person.getPersonId());
    }

    private Campaign createAndSaveCampaign() {

        String suffix =
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8);

        Campaign campaign =
                Campaign.create(
                        CampaignCode.of(
                                "CMP-" + suffix),
                        CampaignName.of(
                                "Campaign " + suffix),
                        CampaignDescription.of(
                                "Integration test campaign"),
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 12, 31));

        return campaignRepository.save(campaign);
    }

    private Country createAndSaveCountry() {

        int sequence =
                COUNTRY_SEQUENCE
                        .incrementAndGet();

        String iso2 =
                toAlphabeticCode(
                        sequence,
                        2);

        String iso3 =
                toAlphabeticCode(
                        sequence,
                        3);

        Country country =
                Country.create(
                        CountryId.generate(),
                        Iso2Code.of(iso2),
                        Iso3Code.of(iso3),
                        NumericCode.of(
                                String.format(
                                        "%03d",
                                        800 + (sequence % 100))),
                        CountryName.of(
                                "Test Country "
                                        + sequence),
                        OfficialCountryName.of(
                                "Official Test Country "
                                        + sequence));

        return countryRepository.save(country);
    }

    private String toAlphabeticCode(
            int value,
            int length) {

        StringBuilder builder =
                new StringBuilder();

        int currentValue =
                value;

        for (int index = 0;
                index < length;
                index++) {

            char letter =
                    (char) ('A'
                            + (currentValue % 26));

            builder.append(letter);

            currentValue =
                    currentValue / 26;
        }

        return builder
                .reverse()
                .toString();
    }

    private Questionnaire createAndSaveQuestionnaire() {

        String suffix =
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 8)
                        .toUpperCase();

        Questionnaire questionnaire =
                Questionnaire.create(
                        QuestionnaireCode.of(
                                "QUESTIONNAIRE_"
                                        + suffix),
                        QuestionnaireName.of(
                                "Questionnaire " + suffix),
                        QuestionnaireDescription.of(
                                "Integration test questionnaire"),
                        QuestionnaireVersion.of(
                                "1.0"),
                        DefaultLanguage.of(
                                "en"),
                        RenderType.FORM);

        return questionnaireRepository.save(
                questionnaire);
    }

    private Organization createAndSaveOrganization(
            Country country) {

        String suffix =
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 8)
                        .toUpperCase();

        Organization organization =
                Organization.create(
                        OrganizationId.generate(),
                        OrganizationCode.of(
                                "ORG_" + suffix),
                        OrganizationName.of(
                                "Organization " + suffix),
                        OrganizationType.MINISTRY,
                        country.getCountryId());

        return organizationRepository.save(
                organization);
    }

    private Person createAndSavePerson(
            Organization organization) {

        String suffix =
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8);

        Person person =
                Person.create(
                        PersonId.generate(),
                        PersonFullName.of(
                                "Test Person " + suffix),
                        organization.getOrganizationId());

        return personRepository.save(person);
    }
}
