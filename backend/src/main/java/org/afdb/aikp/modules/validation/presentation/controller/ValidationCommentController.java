package org.afdb.aikp.modules.validation.presentation.controller;

import org.afdb.aikp.modules.validation.application.dto.CreateValidationCommentDto;
import org.afdb.aikp.modules.validation.application.dto.ValidationCommentDto;
import org.afdb.aikp.modules.validation.application.dto.ObservationCommentCountDto;
import org.afdb.aikp.modules.validation.application.service.ValidationCommentApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/validation-comments")
public class ValidationCommentController {

    private final ValidationCommentApplicationService service;

    public ValidationCommentController(
            ValidationCommentApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ValidationCommentDto create(
            @RequestBody CreateValidationCommentDto request) {

        return service.create(request);
    }

    @GetMapping("/{id}")
    public ValidationCommentDto findById(
            @PathVariable UUID id) {

        return service.findById(id);
    }

    @GetMapping("/observation/{observationId}")
    public List<ValidationCommentDto> findByObservation(
            @PathVariable("observationId") UUID observationId) {

        return service.findByObservation(observationId);
    }


    @GetMapping("/counts/{dataCollectionId}")
    public List<ObservationCommentCountDto> getCounts(
        @PathVariable("dataCollectionId") UUID dataCollectionId
    ) {
        return service.getCounts(dataCollectionId);
    }

}
