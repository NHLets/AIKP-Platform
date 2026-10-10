package org.afdb.aikp.modules.quality.presentation.controller;

import java.util.UUID;

import org.afdb.aikp.modules.collection.domain.valueobject.DataCollectionId;
import org.afdb.aikp.modules.quality.application.dto.QualityAssessmentResponse;
import org.afdb.aikp.modules.quality.application.mapper.QualityAssessmentApplicationMapper;
import org.afdb.aikp.modules.quality.domain.enums.QualityRunTrigger;
import org.afdb.aikp.modules.quality.application.service.QualityAssessmentService;
import org.afdb.aikp.modules.quality.application.service.QualityEngine;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyRole('ADMIN','COORDINATOR','VALIDATOR')")
@RequestMapping("/api/quality/data-collections")
public class QualityAssessmentController {

    private final QualityAssessmentService service;
    private final QualityAssessmentApplicationMapper mapper;
    private final QualityEngine qualityEngine;

    public QualityAssessmentController(
            QualityAssessmentService service,
            QualityAssessmentApplicationMapper mapper,
            QualityEngine qualityEngine) {
        this.service = service;
        this.mapper = mapper;
        this.qualityEngine = qualityEngine;
    }

    @PostMapping("/{dataCollectionId}/assessment")
    public void runAssessment(
            @PathVariable("dataCollectionId") UUID dataCollectionId) {

        qualityEngine.execute(
                DataCollectionId.of(dataCollectionId),
                QualityRunTrigger.MANUAL);
    }

    @GetMapping("/{dataCollectionId}/assessment")
    public QualityAssessmentResponse getAssessment(
            @PathVariable("dataCollectionId") UUID dataCollectionId) {

        return mapper.toResponse(
                service.assess(DataCollectionId.of(dataCollectionId)));
    }
}
