package org.afdb.aikp.modules.collection.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.afdb.aikp.modules.collection.application.observation.command.CreateDataCollectionObservationCommand;
import org.afdb.aikp.modules.collection.application.observation.command.DeleteDataCollectionObservationCommand;
import org.afdb.aikp.modules.collection.application.observation.command.UpdateDataCollectionObservationCommand;
import org.afdb.aikp.modules.collection.application.observation.query.GetDataCollectionObservationQuery;
import org.afdb.aikp.modules.collection.application.observation.query.GetDataCollectionObservationsQuery;
import org.afdb.aikp.modules.collection.application.observation.query.GetObservationByCollectionVariableYearQuery;
import org.afdb.aikp.modules.collection.application.observation.query.GetObservationsByVariableQuery;
import org.afdb.aikp.modules.collection.application.observation.response.DataCollectionObservationResponse;
import org.afdb.aikp.modules.collection.application.observation.service.DataCollectionObservationApplicationService;
import org.afdb.aikp.modules.collection.presentation.request.CreateDataCollectionObservationRequest;
import org.afdb.aikp.modules.collection.presentation.request.UpdateDataCollectionObservationRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

/**
 * REST controller for DataCollectionObservation.
 */
@RestController
@RequestMapping("/api/data-collection-observations")
public class DataCollectionObservationController {

    private final DataCollectionObservationApplicationService service;

    public DataCollectionObservationController(
            DataCollectionObservationApplicationService service) {

        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DataCollectionObservationResponse>
            createObservation(
                    @RequestBody
                    CreateDataCollectionObservationRequest request) {

        CreateDataCollectionObservationCommand command =
                new CreateDataCollectionObservationCommand(
                        request.dataCollectionId(),
                        request.questionnaireVariableId(),
                        request.referenceYear(),
                        request.numericValue(),
                        request.textValue(),
                        request.booleanValue(),
                        request.dateValue(),
                        request.selectedUnit(),
                        request.comment());

        return ResponseEntity
                .status(CREATED)
                .body(service.createProvidedObservation(command));
    }

    @PostMapping("/not-available")
    public ResponseEntity<DataCollectionObservationResponse>
            createNotAvailableObservation(
                    @RequestBody
                    CreateDataCollectionObservationRequest request) {

        CreateDataCollectionObservationCommand command =
                new CreateDataCollectionObservationCommand(
                        request.dataCollectionId(),
                        request.questionnaireVariableId(),
                        request.referenceYear(),
                        null,
                        null,
                        null,
                        null,
                        request.selectedUnit(),
                        request.comment());

        return ResponseEntity
                .status(CREATED)
                .body(service.createNotAvailableObservation(command));
    }

    @PostMapping("/not-applicable")
    public ResponseEntity<DataCollectionObservationResponse>
            createNotApplicableObservation(
                    @RequestBody
                    CreateDataCollectionObservationRequest request) {

        CreateDataCollectionObservationCommand command =
                new CreateDataCollectionObservationCommand(
                        request.dataCollectionId(),
                        request.questionnaireVariableId(),
                        request.referenceYear(),
                        null,
                        null,
                        null,
                        null,
                        request.selectedUnit(),
                        request.comment());

        return ResponseEntity
                .status(CREATED)
                .body(service.createNotApplicableObservation(command));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataCollectionObservationResponse>
            updateObservation(
                    @PathVariable("id") UUID id,
                    @RequestBody
                    UpdateDataCollectionObservationRequest request) {

        UpdateDataCollectionObservationCommand command =
                new UpdateDataCollectionObservationCommand(
                        id,
                        request.numericValue(),
                        request.textValue(),
                        request.booleanValue(),
                        request.dateValue(),
                        request.selectedUnit(),
                        request.comment());

        return ResponseEntity.ok(
                service.updateObservation(command));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteObservation(
            @PathVariable("id") UUID id) {

        service.deleteObservation(
                new DeleteDataCollectionObservationCommand(id));

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataCollectionObservationResponse>
            getObservation(
                    @PathVariable("id") UUID id) {

        return ResponseEntity.ok(
                service.getObservation(
                        new GetDataCollectionObservationQuery(id)));
    }

    @GetMapping("/collection/{dataCollectionId}")
    public ResponseEntity<List<DataCollectionObservationResponse>>
            getObservationsByCollection(
                    @PathVariable("dataCollectionId") UUID dataCollectionId) {

        return ResponseEntity.ok(
                service.getObservations(
                        new GetDataCollectionObservationsQuery(
                                dataCollectionId)));
    }

    @GetMapping("/variable/{questionnaireVariableId}")
    public ResponseEntity<List<DataCollectionObservationResponse>>
            getObservationsByVariable(
                    @PathVariable("questionnaireVariableId") UUID questionnaireVariableId) {

        return ResponseEntity.ok(
                service.getObservationsByVariable(
                        new GetObservationsByVariableQuery(
                                questionnaireVariableId)));
    }

    @GetMapping("/lookup")
    public ResponseEntity<DataCollectionObservationResponse>
            getObservationByCollectionVariableYear(
                    @RequestParam("dataCollectionId") UUID dataCollectionId,
                    @RequestParam("questionnaireVariableId") UUID questionnaireVariableId,
                    @RequestParam("referenceYear") int referenceYear) {

        return ResponseEntity.ok(
                service.getObservationByCollectionVariableYear(
                        new GetObservationByCollectionVariableYearQuery(
                                dataCollectionId,
                                questionnaireVariableId,
                                referenceYear)));
    }
}
