package org.afdb.aikp.modules.quality.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.afdb.aikp.modules.campaign.domain.model.Campaign;
import org.afdb.aikp.modules.campaign.domain.repository.CampaignRepository;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignCode;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignDescription;
import org.afdb.aikp.modules.campaign.domain.valueobject.CampaignName;
import org.afdb.aikp.modules.collection.domain.enums.DataCollectionStatus;
import org.afdb.aikp.modules.collection.domain.model.DataCollection;
import org.afdb.aikp.modules.collection.domain.model.DataCollectionObservation;
import org.afdb.aikp.modules.collection.domain.repository.DataCollectionObservationRepository;
import org.afdb.aikp.modules.collection.domain.repository.DataCollectionRepository;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionObservationId;
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
import org.afdb.aikp.modules.questionnaire.domain.enums.QuestionnaireVariableDataType;
import org.afdb.aikp.modules.questionnaire.domain.model.Questionnaire;
import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireVariable;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireRepository;
import org.afdb.aikp.modules.questionnaire.domain.repository.QuestionnaireVariableRepository;
import org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireCode;
import org.afdb.aikp.modules.quality.domain.enums.QualityFindingStatus;
import org.afdb.aikp.modules.quality.domain.enums.QualityRunStatus;
import org.afdb.aikp.modules.quality.domain.enums.QualityRunTrigger;
import org.afdb.aikp.modules.quality.domain.model.QualityFinding;
import org.afdb.aikp.modules.quality.domain.model.QualityRun;
import org.afdb.aikp.modules.quality.domain.repository.QualityFindingRepository;
import org.afdb.aikp.modules.quality.domain.repository.QualityRunRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class QualityEnginePwaIntegrationTest {

    private static final AtomicInteger COUNTRY_SEQUENCE =
            new AtomicInteger(0);

    private static final int YEAR = 2025;

    private static final List<String> PWA_RULE_CODES = List.of(
            "PWA-001", "PWA-002", "PWA-003",
            "PWA-004", "PWA-005", "PWA-006");

    private static final List<String> BOOLEAN_CODES = List.of(
            "D001", "D002", "D003", "D004", "D005", "D006",
            "D011", "D030", "D036", "D037", "D038", "D039", "D044");

    private static final List<String> REQUIRED_CODES = List.of(
            "D001", "D002", "D003", "D004", "D005", "D006",
            "D007", "D011", "D012", "D017", "D021", "D025",
            "D029", "D030", "D031", "D036", "D037", "D038",
            "D039", "D040", "D044");

    @Autowired
    private QualityEngine qualityEngine;

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private QuestionnaireVariableRepository variableRepository;

    @Autowired
    private DataCollectionRepository dataCollectionRepository;

    @Autowired
    private DataCollectionObservationRepository observationRepository;

    @Autowired
    private QualityFindingRepository qualityFindingRepository;

    @Autowired
    private QualityRunRepository qualityRunRepository;

    @Test
    @Transactional
    void shouldEvaluateAllPwaRulesAndKeepSubmittedStatusWhenDataIsConsistent() {

        Questionnaire questionnaire = findPwaQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveValidPwaData(collection);

        QualityRun run = qualityEngine.execute(
                collection.getDataCollectionId(),
                QualityRunTrigger.SUBMISSION);

        assertThat(run.getStatus())
                .isEqualTo(QualityRunStatus.COMPLETED);

        assertThat(run.getRulesEvaluated())
                .isGreaterThanOrEqualTo(6);

        assertThat(run.getRulesErrored())
                .isZero();

        List<QualityFinding> findings =
                qualityFindingRepository.findByQualityRunId(run.getId());

        assertThat(findings)
                .noneMatch(finding ->
                        PWA_RULE_CODES.contains(finding.getRuleCode()));

        DataCollection persisted =
                dataCollectionRepository
                        .findById(collection.getDataCollectionId())
                        .orElseThrow();

        assertThat(persisted.getStatus())
                .isEqualTo(DataCollectionStatus.SUBMITTED);
    }

    @Test
    @Transactional
    void shouldEvaluateValidatedDataCollection() {

        Questionnaire questionnaire = findPwaQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveValidPwaData(collection);

        collection.validate();
        dataCollectionRepository.save(collection);

        QualityRun run = qualityEngine.execute(
                collection.getDataCollectionId(),
                QualityRunTrigger.MANUAL);

        assertThat(run.getStatus())
                .isEqualTo(QualityRunStatus.COMPLETED);

        assertThat(run.getRulesEvaluated())
                .isGreaterThanOrEqualTo(6);

        assertThat(run.getRulesErrored())
                .isZero();

        DataCollection persisted =
                dataCollectionRepository
                        .findById(collection.getDataCollectionId())
                        .orElseThrow();

        assertThat(persisted.getStatus())
                .isEqualTo(DataCollectionStatus.VALIDATED);
    }

    @Test
    @Transactional
    void shouldKeepOnlyTwoMostRecentQualityRuns() {

        Questionnaire questionnaire = findPwaQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveValidPwaData(collection);

        QualityRun firstRun = qualityEngine.execute(
                collection.getDataCollectionId(),
                QualityRunTrigger.MANUAL);

        QualityRun secondRun = qualityEngine.execute(
                collection.getDataCollectionId(),
                QualityRunTrigger.MANUAL);

        QualityRun thirdRun = qualityEngine.execute(
                collection.getDataCollectionId(),
                QualityRunTrigger.MANUAL);

        List<QualityRun> runs =
                qualityRunRepository.findByDataCollectionId(
                        collection.getDataCollectionId());

        assertThat(runs)
                .hasSize(2);

        assertThat(runs)
                .extracting(QualityRun::getId)
                .containsExactly(
                        thirdRun.getId(),
                        secondRun.getId());

        assertThat(runs)
                .extracting(QualityRun::getId)
                .doesNotContain(firstRun.getId());

        assertThat(
                qualityRunRepository.findById(firstRun.getId()))
                .isEmpty();
    }

    @Test
    @Transactional
    void shouldCreatePwa002FindingWithoutRejectingDataCollectionWhenCodeIsInvalid() {

        Questionnaire questionnaire = findPwaQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveValidPwaDataExcept(collection, "D007");
        saveNumericObservation(collection, "D007", "3");

        QualityRun run = qualityEngine.execute(
                collection.getDataCollectionId(),
                QualityRunTrigger.SUBMISSION);

        assertThat(run.getStatus())
                .isEqualTo(QualityRunStatus.COMPLETED);

        List<QualityFinding> findings =
                qualityFindingRepository.findByQualityRunId(run.getId());

        assertThat(findings)
                .anySatisfy(finding -> {
                    assertThat(finding.getRuleCode())
                            .isEqualTo("PWA-002");
                    assertThat(finding.getStatus())
                            .isEqualTo(QualityFindingStatus.FAILED);
                });

        DataCollection persisted =
                dataCollectionRepository
                        .findById(collection.getDataCollectionId())
                        .orElseThrow();

        assertThat(persisted.getStatus())
                .isEqualTo(DataCollectionStatus.SUBMITTED);
    }

    @Test
    @Transactional
    void shouldCreatePwa001FindingWithoutRejectingDataCollectionWhenRequiredObservationIsMissing() {

        Questionnaire questionnaire = findPwaQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveValidPwaData(collection);

        observationRepository
                .findByDataCollectionId(
                        collection.getDataCollectionId())
                .stream()
                .filter(observation ->
                        variableCode(
                                collection,
                                observation.getQuestionnaireVariableId())
                                .equals("D040"))
                .findFirst()
                .ifPresent(observation ->
                        observationRepository.delete(observation));

        QualityRun run = qualityEngine.execute(
                collection.getDataCollectionId(),
                QualityRunTrigger.SUBMISSION);

        assertThat(run.getStatus())
                .isEqualTo(QualityRunStatus.COMPLETED);

        List<QualityFinding> findings =
                qualityFindingRepository.findByQualityRunId(run.getId());

        assertThat(findings)
                .anySatisfy(finding -> {
                    assertThat(finding.getRuleCode())
                            .isEqualTo("PWA-001");
                    assertThat(finding.getStatus())
                            .isEqualTo(QualityFindingStatus.FAILED);
                });

        DataCollection persisted =
                dataCollectionRepository
                        .findById(collection.getDataCollectionId())
                        .orElseThrow();

        assertThat(persisted.getStatus())
                .isEqualTo(DataCollectionStatus.SUBMITTED);
    }

    private Questionnaire findPwaQuestionnaire() {
        return questionnaireRepository
                .findByCode(QuestionnaireCode.of("PW_A"))
                .orElseThrow(() ->
                        new IllegalStateException(
                                "PW_A questionnaire was not found."));
    }

    private void saveValidPwaData(DataCollection collection) {
        for (String code : REQUIRED_CODES) {
            QuestionnaireVariable variable =
                    findVariable(collection, code);

            if (variable.getDataType()
                    == QuestionnaireVariableDataType.BOOLEAN) {

                saveBooleanObservation(
                        collection,
                        code,
                        false);

            } else if (variable.getDataType()
                    == QuestionnaireVariableDataType.NUMBER
                    || variable.getDataType()
                    == QuestionnaireVariableDataType.INTEGER) {

                saveNumericObservation(
                        collection,
                        code,
                        "1");

            } else {
                throw new IllegalStateException(
                        "Unsupported PW_A variable type for "
                                + code + ": "
                                + variable.getDataType());
            }
        }
    }

    private void saveValidPwaDataExcept(
            DataCollection collection,
            String excludedCode) {

        for (String code : REQUIRED_CODES) {
            if (code.equals(excludedCode)) {
                continue;
            }

            QuestionnaireVariable variable =
                    findVariable(collection, code);

            if (variable.getDataType()
                    == QuestionnaireVariableDataType.BOOLEAN) {

                saveBooleanObservation(
                        collection,
                        code,
                        false);

            } else if (variable.getDataType()
                    == QuestionnaireVariableDataType.NUMBER
                    || variable.getDataType()
                    == QuestionnaireVariableDataType.INTEGER) {

                saveNumericObservation(
                        collection,
                        code,
                        "1");

            } else {
                throw new IllegalStateException(
                        "Unsupported PW_A variable type for "
                                + code + ": "
                                + variable.getDataType());
            }
        }
    }

    private void saveNumericObservation(
            DataCollection collection,
            String seriesCode,
            String value) {

        QuestionnaireVariable variable =
                findVariable(collection, seriesCode);

        DataCollectionObservation observation =
                DataCollectionObservation.provided(
                        DataCollectionObservationId.generate(),
                        collection.getDataCollectionId(),
                        variable.getId(),
                        YEAR,
                        QuestionnaireVariableDataType.NUMBER,
                        new BigDecimal(value),
                        null,
                        null,
                        null,
                        variable.getUnit(),
                        null);

        observationRepository.save(observation);
    }

    private void saveBooleanObservation(
            DataCollection collection,
            String seriesCode,
            boolean value) {

        QuestionnaireVariable variable =
                findVariable(collection, seriesCode);

        DataCollectionObservation observation =
                DataCollectionObservation.provided(
                        DataCollectionObservationId.generate(),
                        collection.getDataCollectionId(),
                        variable.getId(),
                        YEAR,
                        QuestionnaireVariableDataType.BOOLEAN,
                        null,
                        null,
                        value,
                        null,
                        variable.getUnit(),
                        null);

        observationRepository.save(observation);
    }

    private QuestionnaireVariable findVariable(
            DataCollection collection,
            String seriesCode) {

        return variableRepository
                .findByQuestionnaireId(
                        collection.getQuestionnaireId())
                .stream()
                .filter(variable ->
                        variable.getSeriesCode()
                                .equals(seriesCode))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "PW_A variable not found: "
                                        + seriesCode));
    }

    private String variableCode(
            DataCollection collection,
            org.afdb.aikp.modules.questionnaire.domain.valueobject.QuestionnaireVariableId variableId) {

        return variableRepository
                .findByQuestionnaireId(
                        collection.getQuestionnaireId())
                .stream()
                .filter(variable ->
                        variable.getId().equals(variableId))
                .map(QuestionnaireVariable::getSeriesCode)
                .findFirst()
                .orElseThrow();
    }

    private DataCollection createSubmittedDataCollection(
            Questionnaire questionnaire) {

        Campaign campaign = createAndSaveCampaign();
        Country country = createAndSaveCountry();
        Organization organization =
                createAndSaveOrganization(country);

        DataCollection collection =
                DataCollection.create(
                        DataCollectionId.generate(),
                        campaign.getId(),
                        country.getCountryId(),
                        questionnaire.getId(),
                        organization.getOrganizationId(),
                        null,
                        organization.getOrganizationId());

        collection.start();
        collection.submit();

        return dataCollectionRepository.save(collection);
    }

    private Campaign createAndSaveCampaign() {

        String suffix =
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8);

        Campaign campaign =
                Campaign.create(
                        CampaignCode.of("CMP-" + suffix),
                        CampaignName.of("Campaign " + suffix),
                        CampaignDescription.of(
                                "Quality Engine PW_A integration test"),
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 12, 31));

        return campaignRepository.save(campaign);
    }

    private Country createAndSaveCountry() {

        int sequence;
        String iso2;
        String iso3;
        String numericCode;

        do {
            sequence =
                    COUNTRY_SEQUENCE.incrementAndGet();

            iso2 =
                    toAlphabeticCode(
                            sequence + 100,
                            2);

            iso3 =
                    toAlphabeticCode(
                            sequence + 1000,
                            3);

            numericCode =
                    String.format(
                            "%03d",
                            600 + (sequence % 100));

        } while (
                countryRepository.existsByIso2Code(
                        Iso2Code.of(iso2))
                || countryRepository.existsByIso3Code(
                        Iso3Code.of(iso3))
                || countryRepository.existsByNumericCode(
                        NumericCode.of(numericCode)));

        Country country =
                Country.create(
                        CountryId.generate(),
                        Iso2Code.of(iso2),
                        Iso3Code.of(iso3),
                        NumericCode.of(numericCode),
                        CountryName.of(
                                "Test Country " + sequence),
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

        int current = value;

        for (int i = 0; i < length; i++) {
            builder.append(
                    (char) ('A'
                            + (current % 26)));
            current /= 26;
        }

        return builder.reverse().toString();
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
}
