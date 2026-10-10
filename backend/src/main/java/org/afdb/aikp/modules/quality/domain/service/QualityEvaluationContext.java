package org.afdb.aikp.modules.quality.domain.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.afdb.aikp.modules.collection.domain.enums.ObservationStatus;
import org.afdb.aikp.modules.collection.domain.model.DataCollectionObservation;
import org.afdb.aikp.modules.questionnaire.domain.model.QuestionnaireVariable;

public final class QualityEvaluationContext {

    private final Map<String, QuestionnaireVariable> variables;
    private final Map<String, List<DataCollectionObservation>> observations;

    public QualityEvaluationContext(
            Map<String, QuestionnaireVariable> variables,
            Map<String, List<DataCollectionObservation>> observations) {
        this.variables = Map.copyOf(variables);
        this.observations = Map.copyOf(observations);
    }

    public Set<Integer> referenceYears() {
        Set<Integer> years = new TreeSet<>();
        observations.values().forEach(list ->
                list.forEach(o -> years.add(o.getReferenceYear())));
        return Set.copyOf(years);
    }

    public int latestReferenceYear() {
        return referenceYears().stream()
                .max(Integer::compareTo)
                .orElseThrow(() -> new IllegalStateException("No reference year available."));
    }

    public Optional<QuestionnaireVariable> variable(String seriesCode) {
        return Optional.ofNullable(variables.get(normalize(seriesCode)));
    }

    public Optional<DataCollectionObservation> observation(
            String seriesCode,
            int year) {
        return observations.getOrDefault(normalize(seriesCode), List.of())
                .stream()
                .filter(o -> o.getReferenceYear() == year)
                .findFirst();
    }

    public Optional<BigDecimal> numericValue(String seriesCode, int year) {
        return observation(seriesCode, year)
                .filter(o -> o.getStatus() == ObservationStatus.PROVIDED)
                .map(DataCollectionObservation::getNumericValue);
    }

    public Optional<Boolean> booleanValue(String seriesCode, int year) {
        return observation(seriesCode, year)
                .filter(o -> o.getStatus() == ObservationStatus.PROVIDED)
                .map(DataCollectionObservation::getBooleanValue);
    }

    public Optional<String> textValue(String seriesCode, int year) {
        return observation(seriesCode, year)
                .filter(o -> o.getStatus() == ObservationStatus.PROVIDED)
                .map(DataCollectionObservation::getTextValue);
    }

    public Optional<ObservationStatus> observationStatus(
            String seriesCode,
            int year) {
        return observation(seriesCode, year).map(
                DataCollectionObservation::getStatus);
    }

    public Optional<String> unit(String seriesCode) {
        return variable(seriesCode).map(QuestionnaireVariable::getUnit);
    }

    public boolean isActive(String seriesCode) {
        return variable(seriesCode)
                .map(QuestionnaireVariable::isActive)
                .orElse(false);
    }

    private static String normalize(String code) {
        return code == null ? "" : code.trim().toUpperCase();
    }
}
