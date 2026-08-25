package org.afdb.aikp.modules.collection.presentation.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.collection.application.command.CancelDataCollectionCommand;
import org.afdb.aikp.modules.collection.application.command.CreateDataCollectionCommand;
import org.afdb.aikp.modules.collection.application.command.DeleteDataCollectionCommand;
import org.afdb.aikp.modules.collection.application.command.RejectDataCollectionCommand;
import org.afdb.aikp.modules.collection.application.command.StartDataCollectionCommand;
import org.afdb.aikp.modules.collection.application.command.SubmitDataCollectionCommand;
import org.afdb.aikp.modules.collection.application.command.UpdateDataCollectionCommand;
import org.afdb.aikp.modules.collection.application.command.ValidateDataCollectionCommand;
import org.afdb.aikp.modules.collection.application.query.GetDataCollectionQuery;
import org.afdb.aikp.modules.collection.application.query.GetDataCollectionsByCampaignQuery;
import org.afdb.aikp.modules.collection.application.query.GetDataCollectionsByCountryQuery;
import org.afdb.aikp.modules.collection.application.query.GetDataCollectionsQuery;
import org.afdb.aikp.modules.collection.application.response.DataCollectionResponse;
import org.afdb.aikp.modules.collection.application.response.DataCollectionSummary;
import org.afdb.aikp.modules.collection.application.service.DataCollectionApplicationService;
import org.afdb.aikp.modules.collection.presentation.request.CreateDataCollectionRequest;
import org.afdb.aikp.modules.collection.presentation.request.UpdateDataCollectionRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing DataCollection aggregates.
 */
@RestController
@RequestMapping("/api/data-collections")
public class DataCollectionController {

    private final DataCollectionApplicationService service;

    public DataCollectionController(
            DataCollectionApplicationService service) {

        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DataCollectionResponse> createDataCollection(
            @RequestBody CreateDataCollectionRequest request) {

        DataCollectionResponse response =
                service.createDataCollection(
                        new CreateDataCollectionCommand(
                                request.campaignId(),
                                request.countryId(),
                                request.questionnaireId(),
                                request.responsibleOrganizationId(),
                                request.dataCollectorId()));

        return ResponseEntity
                .created(
                        URI.create(
                                "/api/data-collections/"
                                        + response.id()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataCollectionResponse> getDataCollection(
            @PathVariable("id") UUID id) {

        DataCollectionResponse response =
                service.getDataCollection(
                        new GetDataCollectionQuery(id));

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DataCollectionSummary>>
            getDataCollections() {

        List<DataCollectionSummary> response =
                service.getDataCollections(
                        new GetDataCollectionsQuery());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<DataCollectionSummary>>
            getDataCollectionsByCampaign(
                    @PathVariable("campaignId") UUID campaignId) {

        List<DataCollectionSummary> response =
                service.getDataCollectionsByCampaign(
                        new GetDataCollectionsByCampaignQuery(
                                campaignId));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/country/{countryId}")
    public ResponseEntity<List<DataCollectionSummary>>
            getDataCollectionsByCountry(
                    @PathVariable("countryId") UUID countryId) {

        List<DataCollectionSummary> response =
                service.getDataCollectionsByCountry(
                        new GetDataCollectionsByCountryQuery(
                                countryId));

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataCollectionResponse>
            updateDataCollection(
                    @PathVariable("id") UUID id,
                    @RequestBody
                    UpdateDataCollectionRequest request) {

        DataCollectionResponse response =
                service.updateDataCollection(
                        new UpdateDataCollectionCommand(
                                id,
                                request.responsibleOrganizationId(),
                                request.dataCollectorId()));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<DataCollectionResponse>
            startDataCollection(
                    @PathVariable("id") UUID id) {

        DataCollectionResponse response =
                service.startDataCollection(
                        new StartDataCollectionCommand(id));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<DataCollectionResponse>
            submitDataCollection(
                    @PathVariable("id") UUID id) {

        DataCollectionResponse response =
                service.submitDataCollection(
                        new SubmitDataCollectionCommand(id));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/validate")
    public ResponseEntity<DataCollectionResponse>
            validateDataCollection(
                    @PathVariable("id") UUID id) {

        DataCollectionResponse response =
                service.validateDataCollection(
                        new ValidateDataCollectionCommand(id));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<DataCollectionResponse>
            rejectDataCollection(
                    @PathVariable("id") UUID id) {

        DataCollectionResponse response =
                service.rejectDataCollection(
                        new RejectDataCollectionCommand(id));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<DataCollectionResponse>
            cancelDataCollection(
                    @PathVariable("id") UUID id) {

        DataCollectionResponse response =
                service.cancelDataCollection(
                        new CancelDataCollectionCommand(id));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDataCollection(
            @PathVariable("id") UUID id) {

        service.deleteDataCollection(
                new DeleteDataCollectionCommand(id));

        return ResponseEntity.noContent().build();
    }
}
