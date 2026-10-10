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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class QualityEnginePwbIntegrationTest {

    private static final AtomicInteger COUNTRY_SEQUENCE =
            new AtomicInteger(0);

    private static final int YEAR = 2025;

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

    @Test
    @Transactional
    void shouldEvaluatePwb006AndKeepSubmittedStatusWhenDataIsConsistent() {

        Questionnaire questionnaire = findPwbQuestionnaire();

        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B013", "100");
        saveObservation(collection, "B014", "40");
        saveObservation(collection, "B015", "30");
        saveObservation(collection, "B016", "10");
        saveObservation(collection, "B017", "20");

        QualityRun run =
                qualityEngine.execute(
                        collection.getDataCollectionId(),
                        QualityRunTrigger.SUBMISSION);

        assertThat(run.getStatus())
                .isEqualTo(QualityRunStatus.COMPLETED);

        List<QualityFinding> findings =
                qualityFindingRepository.findByQualityRunId(
                        run.getId());

        assertThat(findings)
                .noneMatch(finding ->
                        finding.getRuleCode().equals("PWB-006")
                        && finding.getStatus() != QualityFindingStatus.PASSED);

        DataCollection persisted =
                dataCollectionRepository
                        .findById(collection.getDataCollectionId())
                        .orElseThrow();

        assertThat(persisted.getStatus())
                .isEqualTo(DataCollectionStatus.SUBMITTED);
    }

    @Test
    @Transactional
    void shouldCreateFindingForPwb006WithoutRejectingDataCollectionWhenReconciliationFails() {

        Questionnaire questionnaire = findPwbQuestionnaire();

        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B013", "100");
        saveObservation(collection, "B014", "40");
        saveObservation(collection, "B015", "30");
        saveObservation(collection, "B016", "10");
        saveObservation(collection, "B017", "10");

        QualityRun run =
                qualityEngine.execute(
                        collection.getDataCollectionId(),
                        QualityRunTrigger.SUBMISSION);

        assertThat(run.getStatus())
                .isEqualTo(QualityRunStatus.COMPLETED);

        List<QualityFinding> findings =
                qualityFindingRepository.findByQualityRunId(
                        run.getId());

        assertThat(findings)
                .anySatisfy(finding -> {
                    assertThat(finding.getRuleCode())
                            .isEqualTo("PWB-006");
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
    void shouldEvaluateAllPwbRulesThroughQualityEngine() {

        Questionnaire questionnaire = findPwbQuestionnaire();

        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        // PWB-001 / PWB-002 / PWB-003 / PWB-004
        saveObservation(collection, "B001", "1000");
        saveObservation(collection, "B002", "400");
        saveObservation(collection, "B003", "300");
        saveObservation(collection, "B004", "100");
        saveObservation(collection, "B005", "200");
        saveObservation(collection, "B006", "900");
        saveObservation(collection, "B007", "100");
        saveObservation(collection, "B008", "80");

        // PWB-005 / PWB-007 / PWB-008
        saveObservation(collection, "B024", "1000");
        saveObservation(collection, "B025", "500");
        saveObservation(collection, "B026", "2000");
        saveObservation(collection, "B027", "1000");
        saveObservation(collection, "B028", "3000");
        saveObservation(collection, "B029", "1500");

        // PWB-006
        saveObservation(collection, "B013", "100");
        saveObservation(collection, "B014", "40");
        saveObservation(collection, "B015", "30");
        saveObservation(collection, "B016", "10");
        saveObservation(collection, "B017", "20");

        // PWB-009
        saveObservation(collection, "B034", "0");

        QualityRun run =
                qualityEngine.execute(
                        collection.getDataCollectionId(),
                        QualityRunTrigger.SUBMISSION);

        assertThat(run.getStatus())
                .isEqualTo(QualityRunStatus.COMPLETED);

        assertThat(run.getRulesEvaluated())
                .isGreaterThanOrEqualTo(9);

        assertThat(run.getRulesErrored())
                .isZero();

        List<QualityFinding> findings =
                qualityFindingRepository.findByQualityRunId(
                        run.getId());

        assertThat(findings)
                .noneMatch(finding ->
                        finding.getStatus()
                                == QualityFindingStatus.ERROR);

        List<String> pwbRuleCodes = List.of(
                "PWB-001",
                "PWB-002",
                "PWB-003",
                "PWB-004",
                "PWB-005",
                "PWB-006",
                "PWB-007",
                "PWB-008",
                "PWB-009");

        long pwbFindingCount =
                findings.stream()
                        .filter(finding ->
                                pwbRuleCodes.contains(
                                        finding.getRuleCode()))
                        .count();

        assertThat(pwbFindingCount)
                .isEqualTo(0);

        DataCollection persisted =
                dataCollectionRepository
                        .findById(collection.getDataCollectionId())
                        .orElseThrow();

        assertThat(persisted.getStatus())
                .isEqualTo(DataCollectionStatus.SUBMITTED);
    }


    @Test
    @Transactional
    void shouldCreateFindingForPwb001WhenGenerationCapacityIsNegative() {

        Questionnaire questionnaire = findPwbQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B001", "-1");

        QualityRun run =
                qualityEngine.execute(
                        collection.getDataCollectionId(),
                        QualityRunTrigger.SUBMISSION);

        assertThat(run.getStatus())
                .isEqualTo(QualityRunStatus.COMPLETED);

        assertThat(qualityFindingRepository.findByQualityRunId(run.getId()))
                .anySatisfy(finding -> {
                    assertThat(finding.getRuleCode())
                            .isEqualTo("PWB-001");
                    assertThat(finding.getStatus())
                            .isEqualTo(QualityFindingStatus.FAILED);
                });

        assertDataCollectionRemainsSubmitted(collection);
    }

    @Test
    @Transactional
    void shouldCreateFindingForPwb002WhenGenerationCapacityReconciliationFails() {

        Questionnaire questionnaire = findPwbQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B001", "1000");
        saveObservation(collection, "B002", "400");
        saveObservation(collection, "B003", "300");
        saveObservation(collection, "B004", "100");
        saveObservation(collection, "B005", "100");

        QualityRun run =
                qualityEngine.execute(
                        collection.getDataCollectionId(),
                        QualityRunTrigger.SUBMISSION);

        assertThat(run.getStatus())
                .isEqualTo(QualityRunStatus.COMPLETED);

        assertThat(qualityFindingRepository.findByQualityRunId(run.getId()))
                .anySatisfy(finding -> {
                    assertThat(finding.getRuleCode())
                            .isEqualTo("PWB-002");
                    assertThat(finding.getStatus())
                            .isEqualTo(QualityFindingStatus.FAILED);
                });

        assertDataCollectionRemainsSubmitted(collection);
    }

    @Test
    @Transactional
    void shouldCreateFindingForPwb003WhenOperationalCapacityExceedsInstalledCapacity() {

        Questionnaire questionnaire = findPwbQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B001", "1000");
        saveObservation(collection, "B006", "1100");

        QualityRun run =
                qualityEngine.execute(
                        collection.getDataCollectionId(),
                        QualityRunTrigger.SUBMISSION);

        assertThat(run.getStatus())
                .isEqualTo(QualityRunStatus.COMPLETED);

        assertThat(qualityFindingRepository.findByQualityRunId(run.getId()))
                .anySatisfy(finding -> {
                    assertThat(finding.getRuleCode())
                            .isEqualTo("PWB-003");
                    assertThat(finding.getStatus())
                            .isEqualTo(QualityFindingStatus.FAILED);
                });

        assertDataCollectionRemainsSubmitted(collection);
    }

    @Test
    @Transactional
    void shouldCreateFindingForPwb004WhenOperationalIsolatedCapacityExceedsIsolatedCapacity() {

        Questionnaire questionnaire = findPwbQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B007", "100");
        saveObservation(collection, "B008", "110");

        QualityRun run =
                qualityEngine.execute(
                        collection.getDataCollectionId(),
                        QualityRunTrigger.SUBMISSION);

        assertThat(run.getStatus())
                .isEqualTo(QualityRunStatus.COMPLETED);

        assertThat(qualityFindingRepository.findByQualityRunId(run.getId()))
                .anySatisfy(finding -> {
                    assertThat(finding.getRuleCode())
                            .isEqualTo("PWB-004");
                    assertThat(finding.getStatus())
                            .isEqualTo(QualityFindingStatus.FAILED);
                });

        assertDataCollectionRemainsSubmitted(collection);
    }

    @Test
    @Transactional
    void shouldCreateFindingForPwb005WhenRehabilitatedHvLengthExceedsHvLength() {

        Questionnaire questionnaire = findPwbQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B024", "1000");
        saveObservation(collection, "B025", "1100");

        QualityRun run =
                qualityEngine.execute(
                        collection.getDataCollectionId(),
                        QualityRunTrigger.SUBMISSION);

        assertThat(run.getStatus())
                .isEqualTo(QualityRunStatus.COMPLETED);

        assertThat(qualityFindingRepository.findByQualityRunId(run.getId()))
                .anySatisfy(finding -> {
                    assertThat(finding.getRuleCode())
                            .isEqualTo("PWB-005");
                    assertThat(finding.getStatus())
                            .isEqualTo(QualityFindingStatus.FAILED);
                });

        assertDataCollectionRemainsSubmitted(collection);
    }


    private void assertDataCollectionRemainsSubmitted(
            DataCollection collection) {

        DataCollection persisted =
                dataCollectionRepository
                        .findById(collection.getDataCollectionId())
                        .orElseThrow();

        assertThat(persisted.getStatus())
                .isEqualTo(DataCollectionStatus.SUBMITTED);
    }


    @Test
    @Transactional
    void shouldCreateFindingForPwb007WhenRehabilitatedMvLengthExceedsMvLength() {

        Questionnaire questionnaire = findPwbQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B026", "1000");
        saveObservation(collection, "B027", "1100");

        QualityRun run =
                qualityEngine.execute(
                        collection.getDataCollectionId(),
                        QualityRunTrigger.SUBMISSION);

        assertThat(run.getStatus())
                .isEqualTo(QualityRunStatus.COMPLETED);

        assertThat(qualityFindingRepository.findByQualityRunId(run.getId()))
                .anySatisfy(finding -> {
                    assertThat(finding.getRuleCode())
                            .isEqualTo("PWB-007");
                    assertThat(finding.getStatus())
                            .isEqualTo(QualityFindingStatus.FAILED);
                });

        assertDataCollectionRemainsSubmitted(collection);
    }

    @Test
    @Transactional
    void shouldCreateFindingForPwb008WhenRehabilitatedLvLengthExceedsLvLength() {

        Questionnaire questionnaire = findPwbQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B028", "1000");
        saveObservation(collection, "B029", "1100");

        QualityRun run =
                qualityEngine.execute(
                        collection.getDataCollectionId(),
                        QualityRunTrigger.SUBMISSION);

        assertThat(run.getStatus())
                .isEqualTo(QualityRunStatus.COMPLETED);

        assertThat(qualityFindingRepository.findByQualityRunId(run.getId()))
                .anySatisfy(finding -> {
                    assertThat(finding.getRuleCode())
                            .isEqualTo("PWB-008");
                    assertThat(finding.getStatus())
                            .isEqualTo(QualityFindingStatus.FAILED);
                });

        assertDataCollectionRemainsSubmitted(collection);
    }

    @Test
    @Transactional
    void shouldCreateFindingForPwb009WhenLoadShedIsNegative() {

        Questionnaire questionnaire = findPwbQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B034", "-1");

        QualityRun run =
                qualityEngine.execute(
                        collection.getDataCollectionId(),
                        QualityRunTrigger.SUBMISSION);

        assertThat(run.getStatus())
                .isEqualTo(QualityRunStatus.COMPLETED);

        assertThat(qualityFindingRepository.findByQualityRunId(run.getId()))
                .anySatisfy(finding -> {
                    assertThat(finding.getRuleCode())
                            .isEqualTo("PWB-009");
                    assertThat(finding.getStatus())
                            .isEqualTo(QualityFindingStatus.FAILED);
                });

        assertDataCollectionRemainsSubmitted(collection);
    }


    @Test
    @Transactional
    void shouldMarkPwb006AsNotEvaluableWhenRequiredObservationIsMissing() {

        Questionnaire questionnaire = findPwbQuestionnaire();

        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B013", "100");
        saveObservation(collection, "B014", "40");
        saveObservation(collection, "B015", "30");
        saveObservation(collection, "B016", "10");
        // B017 intentionally missing.

        QualityRun run =
                qualityEngine.execute(
                        collection.getDataCollectionId(),
                        QualityRunTrigger.SUBMISSION);

        assertThat(run.getStatus())
                .isEqualTo(QualityRunStatus.COMPLETED);

        assertThat(run.getRulesNotEvaluable())
                .isGreaterThan(0);

        List<QualityFinding> findings =
                qualityFindingRepository.findByQualityRunId(
                        run.getId());

        assertThat(findings)
                .noneMatch(finding ->
                        finding.getRuleCode().equals("PWB-006")
                        && finding.getStatus()
                                == QualityFindingStatus.FAILED);

        assertDataCollectionRemainsSubmitted(collection);
    }


    @Test
    @Transactional
    void shouldPassPwb006WhenReconciliationDifferenceIsWithinTolerance() {

        Questionnaire questionnaire = findPwbQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B013", "100");
        saveObservation(collection, "B014", "40");
        saveObservation(collection, "B015", "30");
        saveObservation(collection, "B016", "10");
        saveObservation(collection, "B017", "19.6");

        QualityRun run =
                qualityEngine.execute(
                        collection.getDataCollectionId(),
                        QualityRunTrigger.SUBMISSION);

        assertThat(run.getStatus())
                .isEqualTo(QualityRunStatus.COMPLETED);

        assertThat(qualityFindingRepository.findByQualityRunId(run.getId()))
                .noneMatch(finding ->
                        finding.getRuleCode().equals("PWB-006")
                        && finding.getStatus()
                                != QualityFindingStatus.PASSED);

        assertDataCollectionRemainsSubmitted(collection);
    }

    @Test
    @Transactional
    void shouldFailPwb006WhenReconciliationDifferenceExceedsTolerance() {

        Questionnaire questionnaire = findPwbQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B013", "100");
        saveObservation(collection, "B014", "40");
        saveObservation(collection, "B015", "30");
        saveObservation(collection, "B016", "10");
        saveObservation(collection, "B017", "19.4");

        QualityRun run =
                qualityEngine.execute(
                        collection.getDataCollectionId(),
                        QualityRunTrigger.SUBMISSION);

        assertThat(run.getStatus())
                .isEqualTo(QualityRunStatus.COMPLETED);

        assertThat(qualityFindingRepository.findByQualityRunId(run.getId()))
                .anySatisfy(finding -> {
                    assertThat(finding.getRuleCode())
                            .isEqualTo("PWB-006");
                    assertThat(finding.getStatus())
                            .isEqualTo(QualityFindingStatus.FAILED);
                });

        assertDataCollectionRemainsSubmitted(collection);
    }

    private Questionnaire findPwbQuestionnaire() {
        return questionnaireRepository
                .findByCode(QuestionnaireCode.of("PW_B"))
                .orElseThrow(() ->
                        new IllegalStateException(
                                "PW_B questionnaire was not found."));
    }

    private DataCollection createSubmittedDataCollection(
            Questionnaire questionnaire) {

        Campaign campaign =
                createAndSaveCampaign();

        Country country =
                createAndSaveCountry();

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
                        CampaignCode.of(
                                "CMP-" + suffix),
                        CampaignName.of(
                                "Campaign " + suffix),
                        CampaignDescription.of(
                                "Quality Engine PW_B integration test"),
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
                    COUNTRY_SEQUENCE
                            .incrementAndGet();

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

        int current = value;

        for (int i = 0; i < length; i++) {
            builder.append(
                    (char) ('A' + (current % 26)));
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

    private void saveObservation(
            DataCollection collection,
            String seriesCode,
            String value) {

        QuestionnaireVariable variable =
                variableRepository
                        .findByQuestionnaireId(
                                collection.getQuestionnaireId())
                        .stream()
                        .filter(v ->
                                v.getSeriesCode()
                                        .equalsIgnoreCase(
                                                seriesCode))
                        .findFirst()
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "PW_B variable not found: "
                                                + seriesCode));

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
                        "GWh",
                        null);

        observationRepository.save(observation);
    }
}
