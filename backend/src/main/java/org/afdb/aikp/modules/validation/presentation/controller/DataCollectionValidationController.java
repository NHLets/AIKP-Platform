package org.afdb.aikp.modules.validation.presentation.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.validation.application.command.CreateDataCollectionValidationCommand;
import org.afdb.aikp.modules.validation.application.command.DeleteDataCollectionValidationCommand;
import org.afdb.aikp.modules.validation.application.query.GetDataCollectionValidationQuery;
import org.afdb.aikp.modules.validation.application.query.GetDataCollectionValidationsByDataCollectionQuery;
import org.afdb.aikp.modules.validation.application.response.DataCollectionValidationResponse;
import org.afdb.aikp.modules.validation.application.response.DataCollectionValidationSummary;
import org.afdb.aikp.modules.validation.application.service.DataCollectionValidationApplicationService;
import org.afdb.aikp.modules.validation.presentation.request.CreateDataCollectionValidationRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing data collection validations.
 */
@RestController
@RequestMapping("/api/data-collection-validations")
public class DataCollectionValidationController {

    private final DataCollectionValidationApplicationService service;

    public DataCollectionValidationController(
            DataCollectionValidationApplicationService service) {

        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DataCollectionValidationResponse>
            createDataCollectionValidation(
                    @RequestBody
                    CreateDataCollectionValidationRequest request) {

        DataCollectionValidationResponse response =
                service.create(
                        new CreateDataCollectionValidationCommand(
                                request.dataCollectionId(),
                                request.validatorId(),
                                request.decision(),
                                request.comments(),
                                request.validatedAt()));

        return ResponseEntity
                .created(
                        URI.create(
                                "/api/data-collection-validations/"
                                        + response.id()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataCollectionValidationResponse>
            getDataCollectionValidation(
                    @PathVariable("id") UUID id) {

        DataCollectionValidationResponse response =
                service.get(
                        new GetDataCollectionValidationQuery(id));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/data-collection/{dataCollectionId}")
    public ResponseEntity<List<DataCollectionValidationSummary>>
            getDataCollectionValidationsByDataCollection(
                    @PathVariable("dataCollectionId")
                    UUID dataCollectionId) {

        List<DataCollectionValidationSummary> response =
                service.getByDataCollection(
                        new GetDataCollectionValidationsByDataCollectionQuery(
                                dataCollectionId));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDataCollectionValidation(
            @PathVariable("id") UUID id) {

        service.delete(
                new DeleteDataCollectionValidationCommand(id));

        return ResponseEntity.noContent().build();
    }
}
