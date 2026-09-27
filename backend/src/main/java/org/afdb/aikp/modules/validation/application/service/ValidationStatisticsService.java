package org.afdb.aikp.modules.validation.application.service;

import java.util.UUID;
import java.util.List;

import org.springframework.stereotype.Service;

import org.afdb.aikp.modules.collection.infrastructure.persistence.repository.DataCollectionObservationJpaRepository;
import org.afdb.aikp.modules.validation.application.dto.ValidationStatisticsDto;
import org.afdb.aikp.modules.validation.application.dto.QuestionnaireProgressDto;
import org.afdb.aikp.modules.validation.application.dto.ValidationHeatmapDto;
import org.afdb.aikp.modules.validation.application.dto.RejectedObservationDto;
import org.afdb.aikp.modules.validation.application.dto.SeverityDistributionDto;
import org.afdb.aikp.modules.validation.application.dto.ValidationSeverityDto;

@Service
public class ValidationStatisticsService {

    private final DataCollectionObservationJpaRepository repository;

    public ValidationStatisticsService(
        DataCollectionObservationJpaRepository repository
    ) {
        this.repository = repository;
    }

    public ValidationStatisticsDto getStatistics(UUID dataCollectionId) {

        long validated = repository.countByDataCollectionAndObservationStatus(
            dataCollectionId, "VALIDATED");

        long pending = repository.countByDataCollectionAndObservationStatus(
            dataCollectionId, "PENDING");

        long rejected = repository.countByDataCollectionAndObservationStatus(
            dataCollectionId, "REJECTED");

        long notAvailable = repository.countByDataCollectionAndObservationStatus(
            dataCollectionId, "NOT_AVAILABLE");

        long total = repository.countByDataCollection(dataCollectionId);

        double completionRate = total == 0
            ? 0.0
            : ((validated + rejected + notAvailable) * 100.0) / total;

        return new ValidationStatisticsDto(
            validated,
            pending,
            rejected,
            notAvailable,
            total,
            Math.round(completionRate * 10.0) / 10.0
        );
    }


    public List<QuestionnaireProgressDto> getQuestionnaireProgress(
        UUID dataCollectionId
    ) {

        return repository.findQuestionnaireProgress(
            dataCollectionId
        ).stream()
         .map(row -> new QuestionnaireProgressDto(
             (String) row[0],
             (String) row[1],
             ((Number) row[2]).longValue(),
             ((Number) row[3]).longValue(),
             ((Number) row[4]).doubleValue()
         ))
         .toList();
    }



    public List<ValidationHeatmapDto> getValidationHeatmap(
        UUID dataCollectionId
    ) {

        return repository.findValidationHeatmap(
            dataCollectionId
        ).stream()
         .map(row -> new ValidationHeatmapDto(
             (String) row[0],
             (String) row[1],
             ((Number) row[2]).intValue(),
             ((Number) row[3]).longValue()
         ))
         .toList();
    }



    public List<RejectedObservationDto> getRejectedObservations(
        UUID dataCollectionId
    ) {

        return repository.findRejectedObservations(
            dataCollectionId
        ).stream()
         .map(row -> new RejectedObservationDto(
             (UUID) row[0],
             (String) row[1],
             (String) row[2],
             ((Number) row[3]).intValue(),
             row[4] == null ? "INFO" : (String) row[4],
             row[5] == null ? "" : (String) row[5]
         ))
         .toList();
    }



    public List<SeverityDistributionDto> getSeverityDistribution(
        UUID dataCollectionId
    ) {

        return repository.findSeverityDistribution(
            dataCollectionId
        ).stream()
         .map(row -> new SeverityDistributionDto(
             row[0].toString(),
             ((Number) row[1]).longValue()
         ))
         .toList();
    }



    public List<ValidationSeverityDto> getSeverityStatistics(
        UUID dataCollectionId
    ) {

        return repository.findSeverityStatistics(
            dataCollectionId
        ).stream()
         .map(row -> new ValidationSeverityDto(
             (String) row[0],
             ((Number) row[1]).longValue()
         ))
         .toList();
    }

}