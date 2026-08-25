package org.afdb.aikp.modules.person.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.afdb.aikp.modules.person.application.command.ActivatePersonCommand;
import org.afdb.aikp.modules.person.application.command.DeactivatePersonCommand;
import org.afdb.aikp.modules.person.application.command.DeletePersonCommand;
import org.afdb.aikp.modules.person.application.query.GetActivePersonsQuery;
import org.afdb.aikp.modules.person.application.query.GetPersonQuery;
import org.afdb.aikp.modules.person.application.query.GetPersonsByOrganizationQuery;
import org.afdb.aikp.modules.person.application.query.GetPersonsQuery;
import org.afdb.aikp.modules.person.application.response.PersonResponse;
import org.afdb.aikp.modules.person.application.response.PersonSummary;
import org.afdb.aikp.modules.person.application.service.PersonApplicationService;
import org.afdb.aikp.modules.person.presentation.contract.PersonApi;
import org.afdb.aikp.modules.person.presentation.mapper.PersonRestMapper;
import org.afdb.aikp.modules.person.presentation.request.CreatePersonRequest;
import org.afdb.aikp.modules.person.presentation.request.UpdatePersonRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/persons")
@Tag(
        name = "Person",
        description = "Operations for managing persons officially associated with AIKP organizations."
)
public class PersonController implements PersonApi {

    private final PersonApplicationService applicationService;

    public PersonController(
            PersonApplicationService applicationService) {

        this.applicationService = applicationService;
    }

    @Override
    @PostMapping
    @Operation(summary = "Create a new person")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Person created"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request",
                    content = @Content)
    })
    public ResponseEntity<PersonResponse> create(
            @Valid
            @RequestBody
            CreatePersonRequest request) {

        PersonResponse response =
                applicationService.createPerson(
                        PersonRestMapper.toCommand(request));

        URI location =
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(response.id())
                        .toUri();

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @Override
    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a person by its identifier")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Person found"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Person not found",
                    content = @Content)
    })
    public ResponseEntity<PersonResponse> get(
            @Parameter(
                    name = "id",
                    description = "Person identifier",
                    required = true)
            @PathVariable("id")
            UUID id) {

        return ResponseEntity.ok(
                applicationService.getPerson(
                        new GetPersonQuery(id)));
    }

    @Override
    @GetMapping
    @Operation(summary = "Retrieve all persons")
    @ApiResponse(
            responseCode = "200",
            description = "Persons retrieved")
    public ResponseEntity<List<PersonSummary>> getAll() {

        return ResponseEntity.ok(
                applicationService.getPersons(
                        new GetPersonsQuery()));
    }

    @Override
    @GetMapping("/active")
    @Operation(summary = "Retrieve active persons")
    @ApiResponse(
            responseCode = "200",
            description = "Active persons retrieved")
    public ResponseEntity<List<PersonSummary>> getActive() {

        return ResponseEntity.ok(
                applicationService.getActivePersons(
                        new GetActivePersonsQuery()));
    }

    @Override
    @GetMapping("/by-organization/{organizationId}")
    @Operation(
            summary = "Retrieve persons by organization")
    @ApiResponse(
            responseCode = "200",
            description = "Persons retrieved")
    public ResponseEntity<List<PersonSummary>> getByOrganization(
            @Parameter(
                    name = "organizationId",
                    description = "Organization identifier",
                    required = true)
            @PathVariable("organizationId")
            UUID organizationId) {

        return ResponseEntity.ok(
                applicationService.getPersonsByOrganization(
                        new GetPersonsByOrganizationQuery(
                                organizationId)));
    }

    @Override
    @PutMapping("/{id}")
    @Operation(summary = "Update a person")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Person updated"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Person or organization not found",
                    content = @Content)
    })
    public ResponseEntity<PersonResponse> update(
            @PathVariable("id")
            UUID id,

            @Valid
            @RequestBody
            UpdatePersonRequest request) {

        return ResponseEntity.ok(
                applicationService.updatePerson(
                        PersonRestMapper.toCommand(
                                id,
                                request)));
    }

    @Override
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a person")
    @ApiResponse(
            responseCode = "200",
            description = "Person activated")
    public ResponseEntity<PersonResponse> activate(
            @PathVariable("id")
            UUID id) {

        return ResponseEntity.ok(
                applicationService.activatePerson(
                        new ActivatePersonCommand(id)));
    }

    @Override
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a person")
    @ApiResponse(
            responseCode = "200",
            description = "Person deactivated")
    public ResponseEntity<PersonResponse> deactivate(
            @PathVariable("id")
            UUID id) {

        return ResponseEntity.ok(
                applicationService.deactivatePerson(
                        new DeactivatePersonCommand(id)));
    }

    @Override
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a person")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Person deleted"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Person not found",
                    content = @Content)
    })
    public ResponseEntity<Void> delete(
            @PathVariable("id")
            UUID id) {

        applicationService.deletePerson(
                new DeletePersonCommand(id));

        return ResponseEntity.noContent().build();
    }
}
