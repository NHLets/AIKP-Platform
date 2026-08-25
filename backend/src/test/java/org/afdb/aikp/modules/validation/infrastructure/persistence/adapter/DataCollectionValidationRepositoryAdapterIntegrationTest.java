package org.afdb.aikp.modules.validation.infrastructure.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

import org.afdb.aikp.modules.validation.domain.enums.ValidationDecision;
import org.afdb.aikp.modules.validation.domain.model.DataCollectionValidation;
import org.afdb.aikp.modules.validation.domain.repository.DataCollectionValidationRepository;
import org.afdb.aikp.modules.validation.domain.valueobject.DataCollectionValidationId;
import org.afdb.aikp.modules.validation.domain.valueobject.ValidationComments;
import org.afdb.aikp.modules.validation.infrastructure.persistence.repository.DataCollectionValidationJpaRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DataCollectionValidationRepositoryAdapterIntegrationTest {

    @Autowired
    private DataCollectionValidationRepository validationRepository;

    @Autowired
    private DataCollectionValidationJpaRepository
            validationJpaRepository;

    @Autowired
    private DataCollectionRepository dataCollectionRepository;

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private CountryRepository countryRepository;

    @AfterEach
    void cleanUp() {

        validationJpaRepository.deleteAll();
    }

    @Test
    void shouldSaveAndFindValidationById() {

        TestData testData =
                createTestData();

        DataCollectionValidation validation =
                createValidation(
                        testData.dataCollectionId(),
                        testData.personId(),
                        ValidationDecision.VALIDATED,
                        ValidationComments.of(
                                "Validation approved."));

        DataCollectionValidation savedValidation =
                validationRepository.save(validation);

        assertThat(savedValidation)
                .isNotNull();

        assertThat(
                validationRepository.findById(
                        savedValidation
                                .getDataCollectionValidationId()))
                .isPresent()
                .hasValueSatisfying(foundValidation -> {

                    assertThat(
                            foundValidation
                                    .getDataCollectionValidationId())
                            .isEqualTo(
                                    savedValidation
                                            .getDataCollectionValidationId());

                    assertThat(
                            foundValidation
                                    .getDataCollectionId())
                            .isEqualTo(
                                    testData.dataCollectionId());

                    assertThat(
                            foundValidation
                                    .getValidatorId())
                            .isEqualTo(
                                    testData.personId());

                    assertThat(
                            foundValidation
                                    .getDecision())
                            .isEqualTo(
                                    ValidationDecision.VALIDATED);

                    assertThat(
                            foundValidation
                                    .getComments()
                                    .getValue())
                            .isEqualTo(
                                    "Validation approved.");
                });
    }

    @Test
    void shouldFindValidationsByDataCollectionId() {

        TestData targetTestData =
                createTestData();

        TestData otherTestData =
                createTestData();

        DataCollectionValidation firstValidation =
                validationRepository.save(
                        createValidation(
                                targetTestData
                                        .dataCollectionId(),
                                targetTestData
                                        .personId(),
                                ValidationDecision.VALIDATED,
                                ValidationComments.of(
                                        "Approved.")));

        DataCollectionValidation secondValidation =
                validationRepository.save(
                        createValidation(
                                targetTestData
                                        .dataCollectionId(),
                                targetTestData
                                        .personId(),
                                ValidationDecision.REJECTED,
                                ValidationComments.of(
                                        "Rejected.")));

        validationRepository.save(
                createValidation(
                        otherTestData
                                .dataCollectionId(),
                        otherTestData
                                .personId(),
                        ValidationDecision.VALIDATED,
                        null));

        List<DataCollectionValidation> validations =
                validationRepository.findByDataCollectionId(
                        targetTestData.dataCollectionId());

        assertThat(validations)
                .hasSize(2);

        assertThat(validations)
                .extracting(
                        DataCollectionValidation
                                ::getDataCollectionValidationId)
                .containsExactlyInAnyOrder(
                        firstValidation
                                .getDataCollectionValidationId(),
                        secondValidation
                                .getDataCollectionValidationId());
    }

    @Test
    void shouldReturnTrueWhenValidationExists() {

        TestData testData =
                createTestData();

        DataCollectionValidation savedValidation =
                validationRepository.save(
                        createValidation(
                                testData.dataCollectionId(),
                                testData.personId(),
                                ValidationDecision.VALIDATED,
                                null));

        boolean exists =
                validationRepository.existsById(
                        savedValidation
                                .getDataCollectionValidationId());

        assertThat(exists)
                .isTrue();
    }

    @Test
    void shouldReturnFalseWhenValidationDoesNotExist() {

        boolean exists =
                validationRepository.existsById(
                        DataCollectionValidationId.of(
                                UUID.randomUUID()));

        assertThat(exists)
                .isFalse();
    }

    @Test
    void shouldDeleteValidation() {

        TestData testData =
                createTestData();

        DataCollectionValidation savedValidation =
                validationRepository.save(
                        createValidation(
                                testData.dataCollectionId(),
                                testData.personId(),
                                ValidationDecision.REJECTED,
                                ValidationComments.of(
                                        "Incomplete data.")));

        validationRepository.delete(savedValidation);

        assertThat(
                validationRepository.existsById(
                        savedValidation
                                .getDataCollectionValidationId()))
                .isFalse();

        assertThat(
                validationRepository.findById(
                        savedValidation
                                .getDataCollectionValidationId()))
                .isEmpty();
    }

    private TestData createTestData() {

        String suffix =
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();

        CountryId countryId =
                findOrCreateMadagascar();

        OrganizationId organizationId =
                OrganizationId.generate();

        Organization organization =
                Organization.create(
                        organizationId,
                        OrganizationCode.of(
                                "ORG-" + suffix),
                        OrganizationName.of(
                                "Test Organization " + suffix),
                        OrganizationType.GOVERNMENT_AGENCY,
                        countryId);

        organizationRepository.save(organization);

        PersonId personId =
                PersonId.generate();

        Person person =
                Person.create(
                        personId,
                        PersonFullName.of(
                                "Test Person " + suffix),
                        organizationId);

        personRepository.save(person);

        Campaign campaign =
                Campaign.create(
                        CampaignCode.of(
                                "CMP-" + suffix),
                        CampaignName.of(
                                "Test Campaign " + suffix),
                        CampaignDescription.of(
                                "Campaign created for validation "
                                        + "repository integration tests."),
                        LocalDate.now(),
                        LocalDate.now().plusDays(30));

        campaignRepository.save(campaign);

        Questionnaire questionnaire =
                Questionnaire.create(
                        QuestionnaireCode.of(
                                "QST_" + suffix),
                        QuestionnaireName.of(
                                "Test Questionnaire " + suffix),
                        QuestionnaireDescription.of(
                                "Questionnaire created for validation "
                                        + "repository integration tests."),
                        QuestionnaireVersion.of("1.0"),
                        DefaultLanguage.of("en"),
                        RenderType.FORM);

        questionnaireRepository.save(questionnaire);

        DataCollectionId dataCollectionId =
                DataCollectionId.generate();

        DataCollection dataCollection =
                DataCollection.create(
                        dataCollectionId,
                        campaign.getId(),
                        countryId,
                        questionnaire.getId(),
                        organizationId,
                        personId);

        dataCollectionRepository.save(dataCollection);

        return new TestData(
                dataCollectionId,
                personId);
    }

    private CountryId findOrCreateMadagascar() {

        return countryRepository
                .findByIso2Code(
                        Iso2Code.of("MG"))
                .map(Country::getCountryId)
                .orElseGet(() -> {

                    CountryId countryId =
                            CountryId.generate();

                    Country country =
                            Country.create(
                                    countryId,
                                    Iso2Code.of("MG"),
                                    Iso3Code.of("MDG"),
                                    NumericCode.of("450"),
                                    CountryName.of(
                                            "Madagascar"),
                                    OfficialCountryName.of(
                                            "Republic of Madagascar"));

                    countryRepository.save(country);

                    return countryId;
                });
    }

    private DataCollectionValidation createValidation(
            DataCollectionId dataCollectionId,
            PersonId validatorId,
            ValidationDecision decision,
            ValidationComments comments) {

        return DataCollectionValidation.create(
                DataCollectionValidationId.generate(),
                dataCollectionId,
                validatorId,
                decision,
                comments,
                Instant.now());
    }

    private record TestData(
            DataCollectionId dataCollectionId,
            PersonId personId) {
    }
}
