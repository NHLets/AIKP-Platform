package org.afdb.aikp.modules.organization.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.afdb.aikp.modules.organization.application.command.ActivateOrganizationCommand;
import org.afdb.aikp.modules.organization.application.command.DeactivateOrganizationCommand;
import org.afdb.aikp.modules.organization.application.command.DeleteOrganizationCommand;
import org.afdb.aikp.modules.organization.application.query.GetActiveOrganizationsQuery;
import org.afdb.aikp.modules.organization.application.query.GetOrganizationQuery;
import org.afdb.aikp.modules.organization.application.query.GetOrganizationsByCountryQuery;
import org.afdb.aikp.modules.organization.application.query.GetOrganizationsByTypeQuery;
import org.afdb.aikp.modules.organization.application.query.GetOrganizationsQuery;
import org.afdb.aikp.modules.organization.application.response.OrganizationResponse;
import org.afdb.aikp.modules.organization.application.response.OrganizationSummary;
import org.afdb.aikp.modules.organization.application.service.OrganizationApplicationService;
import org.afdb.aikp.modules.organization.presentation.contract.OrganizationApi;
import org.afdb.aikp.modules.organization.presentation.mapper.OrganizationRestMapper;
import org.afdb.aikp.modules.organization.presentation.request.CreateOrganizationRequest;
import org.afdb.aikp.modules.organization.presentation.request.UpdateOrganizationRequest;
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
@RequestMapping("/api/v1/organizations")
@Tag(
        name = "Organization",
        description = "Operations for managing AIKP organizations."
)
public class OrganizationController implements OrganizationApi {

    private final OrganizationApplicationService applicationService;

    public OrganizationController(
            OrganizationApplicationService applicationService) {

        this.applicationService = applicationService;
    }

    @Override
    @PostMapping
    @Operation(summary = "Create a new organization")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Organization created"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "409",
                    description = "Organization code already exists",
                    content = @Content)
    })
    public ResponseEntity<OrganizationResponse> create(
            @Valid
            @RequestBody
            CreateOrganizationRequest request) {

        OrganizationResponse response =
                applicationService.create(
                        OrganizationRestMapper.toCommand(request));

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
    @Operation(summary = "Retrieve an organization by its identifier")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Organization found"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Organization not found",
                    content = @Content)
    })
    public ResponseEntity<OrganizationResponse> get(

            @Parameter(
                    name = "id",
                    description = "Organization identifier",
                    required = true,
                    example = "901308db-4448-4b9e-a2f7-c0f899e10b6b")
            @PathVariable("id")
            UUID id) {

        return ResponseEntity.ok(
                applicationService.get(
                        new GetOrganizationQuery(id)));
    }

    @Override
    @GetMapping
    @Operation(summary = "Retrieve all organizations")
    @ApiResponse(
            responseCode = "200",
            description = "Organizations retrieved")
    public ResponseEntity<List<OrganizationSummary>> getAll() {

        return ResponseEntity.ok(
                applicationService.getAll(
                        new GetOrganizationsQuery()));
    }

    @Override
    @GetMapping("/active")
    @Operation(summary = "Retrieve active organizations")
    @ApiResponse(
            responseCode = "200",
            description = "Active organizations retrieved")
    public ResponseEntity<List<OrganizationSummary>> getActive() {

        return ResponseEntity.ok(
                applicationService.getActive(
                        new GetActiveOrganizationsQuery()));
    }

    @Override
    @GetMapping("/by-country/{countryId}")
    @Operation(
            summary = "Retrieve organizations by country")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Organizations retrieved")
    })
    public ResponseEntity<List<OrganizationSummary>> getByCountry(

            @Parameter(
                    name = "countryId",
                    description = "Country identifier",
                    required = true,
                    example = "11111111-1111-1111-1111-111111111111")
            @PathVariable("countryId")
            UUID countryId) {

        return ResponseEntity.ok(
                applicationService.getByCountry(
                        new GetOrganizationsByCountryQuery(countryId)));
    }

    @Override
    @GetMapping("/by-type/{type}")
    @Operation(
            summary = "Retrieve organizations by type")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Organizations retrieved")
    })
    public ResponseEntity<List<OrganizationSummary>> getByType(

            @Parameter(
                    name = "type",
                    description = "Organization type",
                    required = true,
                    example = "DEVELOPMENT_PARTNER")
            @PathVariable("type")
            String type) {

        return ResponseEntity.ok(
                applicationService.getByType(
                        new GetOrganizationsByTypeQuery(type)));
    }

    @Override
    @PutMapping("/{id}")
    @Operation(summary = "Update an organization")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Organization updated"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Organization not found",
                    content = @Content),
            @ApiResponse(
                    responseCode = "409",
                    description = "Organization code already exists",
                    content = @Content)
    })
    public ResponseEntity<OrganizationResponse> update(

            @Parameter(
                    name = "id",
                    description = "Organization identifier",
                    required = true,
                    example = "901308db-4448-4b9e-a2f7-c0f899e10b6b")
            @PathVariable("id")
            UUID id,

            @Valid
            @RequestBody
            UpdateOrganizationRequest request) {

        return ResponseEntity.ok(
                applicationService.update(
                        OrganizationRestMapper.toCommand(
                                id,
                                request)));
    }

    @Override
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate an organization")
    @ApiResponse(
            responseCode = "200",
            description = "Organization activated")
    public ResponseEntity<OrganizationResponse> activate(

            @Parameter(
                    name = "id",
                    description = "Organization identifier",
                    required = true,
                    example = "901308db-4448-4b9e-a2f7-c0f899e10b6b")
            @PathVariable("id")
            UUID id) {

        return ResponseEntity.ok(
                applicationService.activate(
                        new ActivateOrganizationCommand(id)));
    }

    @Override
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate an organization")
    @ApiResponse(
            responseCode = "200",
            description = "Organization deactivated")
    public ResponseEntity<OrganizationResponse> deactivate(

            @Parameter(
                    name = "id",
                    description = "Organization identifier",
                    required = true,
                    example = "901308db-4448-4b9e-a2f7-c0f899e10b6b")
            @PathVariable("id")
            UUID id) {

        return ResponseEntity.ok(
                applicationService.deactivate(
                        new DeactivateOrganizationCommand(id)));
    }

    @Override
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an organization")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Organization deleted"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Organization not found",
                    content = @Content)
    })
    public ResponseEntity<Void> delete(

            @Parameter(
                    name = "id",
                    description = "Organization identifier",
                    required = true,
                    example = "901308db-4448-4b9e-a2f7-c0f899e10b6b")
            @PathVariable("id")
            UUID id) {

        applicationService.delete(
                new DeleteOrganizationCommand(id));

        return ResponseEntity.noContent().build();
    }
}
