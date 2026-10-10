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
class QualityEnginePwcIntegrationTest {

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
    void shouldEvaluatePwc001WhenPotentialCustomersReconcile() {

        Questionnaire questionnaire = findPwcQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "A214", "1000");
        saveObservation(collection, "A261", "600");
        saveObservation(collection, "A262", "400");

        QualityRun run = execute(collection);

        assertCompleted(run);
        assertRuleHasNoFailedFinding(run, "PWC-001");
        assertDataCollectionRemainsSubmitted(collection);
    }

    @Test
    @Transactional
    void shouldCreateFindingForPwc001WhenPotentialCustomersDoNotReconcile() {

        Questionnaire questionnaire = findPwcQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "A214", "1000");
        saveObservation(collection, "A261", "600");
        saveObservation(collection, "A262", "300");

        QualityRun run = execute(collection);

        assertCompleted(run);
        assertRuleFailed(run, "PWC-001");
        assertDataCollectionRemainsSubmitted(collection);
    }

    @Test
    @Transactional
    void shouldCreateFindingForPwc002WhenOperatingMeteredCustomersExceedMeteredCustomers() {

        Questionnaire questionnaire = findPwcQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B048", "1000");
        saveObservation(collection, "B049", "1100");

        QualityRun run = execute(collection);

        assertCompleted(run);
        assertRuleFailed(run, "PWC-002");
        assertDataCollectionRemainsSubmitted(collection);
    }

    @Test
    @Transactional
    void shouldCreateFindingForPwc003WhenOperatingPrepaymentCustomersExceedPrepaymentCustomers() {

        Questionnaire questionnaire = findPwcQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B050", "1000");
        saveObservation(collection, "B051", "1100");

        QualityRun run = execute(collection);

        assertCompleted(run);
        assertRuleFailed(run, "PWC-003");
        assertDataCollectionRemainsSubmitted(collection);
    }

    @Test
    @Transactional
    void shouldCreateFindingForPwc004WhenSystemLossesDoNotReconcile() {

        Questionnaire questionnaire = findPwcQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B052", "100");
        saveObservation(collection, "B053", "60");
        saveObservation(collection, "B054", "20");

        QualityRun run = execute(collection);

        assertCompleted(run);
        assertRuleFailed(run, "PWC-004");
        assertDataCollectionRemainsSubmitted(collection);
    }

    @Test
    @Transactional
    void shouldCreateFindingForPwc005WhenTechnicalLossesDoNotReconcile() {

        Questionnaire questionnaire = findPwcQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B053", "60");
        saveObservation(collection, "B055", "20");
        saveObservation(collection, "B056", "30");

        QualityRun run = execute(collection);

        assertCompleted(run);
        assertRuleFailed(run, "PWC-005");
        assertDataCollectionRemainsSubmitted(collection);
    }

    @Test
    @Transactional
    void shouldCreateFindingForPwc006WhenCollectedBillsExceedBilling() {

        Questionnaire questionnaire = findPwcQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B061", "110");
        saveObservation(collection, "B069", "100");

        QualityRun run = execute(collection);

        assertCompleted(run);
        assertRuleFailed(run, "PWC-006");
        assertDataCollectionRemainsSubmitted(collection);
    }

    @Test
    @Transactional
    void shouldCreateFindingForPwc007WhenOperationalCostComponentsExceedOperationalCosts() {

        Questionnaire questionnaire = findPwcQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B076", "100");
        saveObservation(collection, "B077", "40");
        saveObservation(collection, "B078", "30");
        saveObservation(collection, "B079", "40");

        QualityRun run = execute(collection);

        assertCompleted(run);
        assertRuleFailed(run, "PWC-007");
        assertDataCollectionRemainsSubmitted(collection);
    }

    @Test
    @Transactional
    void shouldCreateFindingForPwc008WhenRehabilitationCostsExceedCapitalCost() {

        Questionnaire questionnaire = findPwcQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B081", "100");
        saveObservation(collection, "B082", "110");

        QualityRun run = execute(collection);

        assertCompleted(run);
        assertRuleFailed(run, "PWC-008");
        assertDataCollectionRemainsSubmitted(collection);
    }

    @Test
    @Transactional
    void shouldCreateFindingForPwc009WhenNewAssetsInvestmentExceedsCapitalCost() {

        Questionnaire questionnaire = findPwcQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B081", "100");
        saveObservation(collection, "B083", "110");

        QualityRun run = execute(collection);

        assertCompleted(run);
        assertRuleFailed(run, "PWC-009");
        assertDataCollectionRemainsSubmitted(collection);
    }

    @Test
    @Transactional
    void shouldEvaluateAllPwcRulesThroughQualityEngine() {

        Questionnaire questionnaire = findPwcQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        // PWC-001
        saveObservation(collection, "A214", "1000");
        saveObservation(collection, "A261", "600");
        saveObservation(collection, "A262", "400");

        // PWC-002 / PWC-003
        saveObservation(collection, "B048", "1000");
        saveObservation(collection, "B049", "900");
        saveObservation(collection, "B050", "800");
        saveObservation(collection, "B051", "700");

        // PWC-004 / PWC-005
        saveObservation(collection, "B052", "100");
        saveObservation(collection, "B053", "60");
        saveObservation(collection, "B054", "40");
        saveObservation(collection, "B055", "20");
        saveObservation(collection, "B056", "40");

        // PWC-006
        saveObservation(collection, "B061", "90");
        saveObservation(collection, "B069", "100");

        // PWC-007
        saveObservation(collection, "B076", "100");
        saveObservation(collection, "B077", "30");
        saveObservation(collection, "B078", "20");
        saveObservation(collection, "B079", "40");

        // PWC-008 / PWC-009
        saveObservation(collection, "B081", "100");
        saveObservation(collection, "B082", "40");
        saveObservation(collection, "B083", "50");

        QualityRun run = execute(collection);

        assertCompleted(run);

        assertThat(run.getRulesEvaluated())
                .isGreaterThanOrEqualTo(9);

        assertThat(run.getRulesErrored())
                .isZero();

        List<QualityFinding> findings =
                qualityFindingRepository.findByQualityRunId(run.getId());

        assertThat(findings)
                .noneMatch(finding ->
                        finding.getStatus()
                                == QualityFindingStatus.ERROR);

        List<String> pwcRuleCodes = List.of(
                "PWC-001",
                "PWC-002",
                "PWC-003",
                "PWC-004",
                "PWC-005",
                "PWC-006",
                "PWC-007",
                "PWC-008",
                "PWC-009");

        assertThat(findings.stream()
                .filter(finding ->
                        pwcRuleCodes.contains(finding.getRuleCode()))
                .count())
                .isZero();

        assertDataCollectionRemainsSubmitted(collection);
    }

    @Test
    @Transactional
    void shouldMarkPwc001AsNotEvaluableWhenRequiredObservationIsMissing() {

        Questionnaire questionnaire = findPwcQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "A214", "1000");
        saveObservation(collection, "A261", "600");
        // A262 intentionally missing.

        QualityRun run = execute(collection);

        assertCompleted(run);

        assertThat(run.getRulesNotEvaluable())
                .isGreaterThan(0);

        List<QualityFinding> findings =
                qualityFindingRepository.findByQualityRunId(run.getId());

        assertThat(findings)
                .noneMatch(finding ->
                        finding.getRuleCode().equals("PWC-001")
                        && finding.getStatus()
                                == QualityFindingStatus.FAILED);

        assertDataCollectionRemainsSubmitted(collection);
    }

    @Test
    @Transactional
    void shouldMarkPwc007AsNotEvaluableWhenRequiredObservationIsMissing() {

        Questionnaire questionnaire = findPwcQuestionnaire();
        DataCollection collection =
                createSubmittedDataCollection(questionnaire);

        saveObservation(collection, "B076", "100");
        saveObservation(collection, "B077", "30");
        saveObservation(collection, "B078", "20");
        // B079 intentionally missing.

        QualityRun run = execute(collection);

        assertCompleted(run);

        assertThat(run.getRulesNotEvaluable())
                .isGreaterThan(0);

        List<QualityFinding> findings =
                qualityFindingRepository.findByQualityRunId(run.getId());

        assertThat(findings)
                .noneMatch(finding ->
                        finding.getRuleCode().equals("PWC-007")
                        && finding.getStatus()
                                == QualityFindingStatus.FAILED);

        assertDataCollectionRemainsSubmitted(collection);
    }

    private QualityRun execute(DataCollection collection) {
        return qualityEngine.execute(
                collection.getDataCollectionId(),
                QualityRunTrigger.SUBMISSION);
    }

    private void assertCompleted(QualityRun run) {
        assertThat(run.getStatus())
                .isEqualTo(QualityRunStatus.COMPLETED);
    }

    private void assertRuleFailed(
            QualityRun run,
            String ruleCode) {

        assertThat(
                qualityFindingRepository.findByQualityRunId(
                        run.getId()))
                .anySatisfy(finding -> {
                    assertThat(finding.getRuleCode())
                            .isEqualTo(ruleCode);
                    assertThat(finding.getStatus())
                            .isEqualTo(QualityFindingStatus.FAILED);
                });
    }

    private void assertRuleHasNoFailedFinding(
            QualityRun run,
            String ruleCode) {

        assertThat(
                qualityFindingRepository.findByQualityRunId(
                        run.getId()))
                .noneMatch(finding ->
                        finding.getRuleCode().equals(ruleCode)
                        && finding.getStatus()
                                != QualityFindingStatus.PASSED);
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

    private Questionnaire findPwcQuestionnaire() {
        return questionnaireRepository
                .findByCode(QuestionnaireCode.of("PW_C"))
                .orElseThrow(() ->
                        new IllegalStateException(
                                "PW_C questionnaire was not found."));
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
                                "Quality Engine PW_C integration test"),
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
            sequence = COUNTRY_SEQUENCE.incrementAndGet();

            iso2 = toAlphabeticCode(sequence + 100, 2);
            iso3 = toAlphabeticCode(sequence + 1000, 3);

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
                                "Official Test Country " + sequence));

        return countryRepository.save(country);
    }

    private String toAlphabeticCode(
            int value,
            int length) {

        StringBuilder builder = new StringBuilder();
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
                        .substring(0, 8);

        Organization organization =
                Organization.create(
                        OrganizationId.generate(),
                        OrganizationCode.of("ORG-" + suffix),
                        OrganizationName.of(
                                "Quality Test Organization " + suffix),
                        OrganizationType.UTILITY,
                        country.getCountryId());

        return organizationRepository.save(organization);
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
                                        "PW_C variable not found: "
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
                        variable.getUnit(),
                        null);

        observationRepository.save(observation);
    }
}
