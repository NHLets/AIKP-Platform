package org.afdb.aikp.modules.validation.presentation.controller;

import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import org.afdb.aikp.modules.validation.application.dto.ValidationStatisticsDto;
import org.afdb.aikp.modules.validation.application.service.ValidationStatisticsService;
import java.util.List;
import org.afdb.aikp.modules.validation.application.dto.QuestionnaireProgressDto;
import org.afdb.aikp.modules.validation.application.dto.ValidationHeatmapDto;
import org.afdb.aikp.modules.validation.application.dto.RejectedObservationDto;
import org.afdb.aikp.modules.validation.application.dto.SeverityDistributionDto;
import org.afdb.aikp.modules.validation.application.dto.ValidationSeverityDto;

@RestController
@PreAuthorize(\"hasAnyRole('ADMIN','COORDINATOR','VALIDATOR')\")
@RequestMapping("/api/validation/statistics")
public class ValidationStatisticsController {

    private final ValidationStatisticsService service;

    public ValidationStatisticsController(
        ValidationStatisticsService service
    ) {
        this.service = service;
    }

    @GetMapping("/{dataCollectionId}")
    public ValidationStatisticsDto getStatistics(
        @PathVariable("dataCollectionId") UUID dataCollectionId
    ) {
        return service.getStatistics(dataCollectionId);
    }


    @GetMapping("/{dataCollectionId}/questionnaires")
    public List<QuestionnaireProgressDto> getQuestionnaireProgress(
        @PathVariable("dataCollectionId") UUID dataCollectionId
    ) {
        return service.getQuestionnaireProgress(
            dataCollectionId
        );
    }



    @GetMapping("/{dataCollectionId}/heatmap")
    public List<ValidationHeatmapDto> getValidationHeatmap(
        @PathVariable("dataCollectionId") UUID dataCollectionId
    ) {
        return service.getValidationHeatmap(
            dataCollectionId
        );
    }



    @GetMapping("/{dataCollectionId}/rejected")
    public List<RejectedObservationDto> getRejectedObservations(
        @PathVariable("dataCollectionId") UUID dataCollectionId
    ) {
        return service.getRejectedObservations(
            dataCollectionId
        );
    }



    @GetMapping("/{dataCollectionId}/severity")
    public List<SeverityDistributionDto> getSeverityDistribution(
        @PathVariable("dataCollectionId") UUID dataCollectionId
    ) {
        return service.getSeverityDistribution(dataCollectionId);
    }



    @GetMapping("/{dataCollectionId}/severity")
    public List<ValidationSeverityDto> getSeverityStatistics(
        @PathVariable("dataCollectionId") UUID dataCollectionId
    ) {
        return service.getSeverityStatistics(
            dataCollectionId
        );
    }

}