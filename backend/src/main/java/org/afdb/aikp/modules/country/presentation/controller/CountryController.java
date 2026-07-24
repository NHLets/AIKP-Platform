package org.afdb.aikp.modules.country.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.afdb.aikp.modules.country.application.command.ActivateCountryCommand;
import org.afdb.aikp.modules.country.application.command.DeactivateCountryCommand;
import org.afdb.aikp.modules.country.application.command.DeleteCountryCommand;
import org.afdb.aikp.modules.country.application.query.GetActiveCountriesQuery;
import org.afdb.aikp.modules.country.application.query.GetCountriesQuery;
import org.afdb.aikp.modules.country.application.query.GetCountryQuery;
import org.afdb.aikp.modules.country.application.response.CountryResponse;
import org.afdb.aikp.modules.country.application.response.CountrySummary;
import org.afdb.aikp.modules.country.application.service.CountryApplicationService;
import org.afdb.aikp.modules.country.presentation.contract.CountryApi;
import org.afdb.aikp.modules.country.presentation.mapper.CountryRestMapper;
import org.afdb.aikp.modules.country.presentation.request.CreateCountryRequest;
import org.afdb.aikp.modules.country.presentation.request.UpdateCountryRequest;
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
@RequestMapping("/api/v1/countries")
@Tag(
        name = "Country",
        description = "Operations for managing ISO 3166-1 countries."
)
public class CountryController implements CountryApi {

    private final CountryApplicationService applicationService;

    public CountryController(CountryApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @Override
    @PostMapping
    @Operation(summary = "Create a new country")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Country created"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "409", description = "Country already exists", content = @Content)
    })
    public ResponseEntity<CountryResponse> create(
            @Valid
            @RequestBody
            CreateCountryRequest request) {

        CountryResponse response =
                applicationService.create(
                        CountryRestMapper.toCommand(request));

        URI location = ServletUriComponentsBuilder
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
    @Operation(summary = "Retrieve a country by its identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Country found"),
            @ApiResponse(responseCode = "404", description = "Country not found", content = @Content)
    })
    public ResponseEntity<CountryResponse> get(

            @Parameter(
                    name = "id",
                    description = "Country identifier",
                    required = true,
                    example = "901308db-4448-4b9e-a2f7-c0f899e10b6b")
            @PathVariable("id")
            UUID id) {

        return ResponseEntity.ok(
                applicationService.get(
                        new GetCountryQuery(id)));
    }

    @Override
    @GetMapping
    @Operation(summary = "Retrieve all countries")
    @ApiResponse(responseCode = "200", description = "Countries retrieved")
    public ResponseEntity<List<CountrySummary>> getAll() {

        return ResponseEntity.ok(
                applicationService.getAll(
                        new GetCountriesQuery()));
    }

    @Override
    @GetMapping("/active")
    @Operation(summary = "Retrieve active countries")
    @ApiResponse(responseCode = "200", description = "Active countries retrieved")
    public ResponseEntity<List<CountrySummary>> getActive() {

        return ResponseEntity.ok(
                applicationService.getActive(
                        new GetActiveCountriesQuery()));
    }

    @Override
    @PutMapping("/{id}")
    @Operation(summary = "Update a country")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Country updated"),
            @ApiResponse(responseCode = "404", description = "Country not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Duplicate country", content = @Content)
    })
    public ResponseEntity<CountryResponse> update(

            @Parameter(
                    name = "id",
                    description = "Country identifier",
                    required = true,
                    example = "901308db-4448-4b9e-a2f7-c0f899e10b6b")
            @PathVariable("id")
            UUID id,

            @Valid
            @RequestBody
            UpdateCountryRequest request) {

        return ResponseEntity.ok(
                applicationService.update(
                        CountryRestMapper.toCommand(id, request)));
    }

    @Override
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a country")
    @ApiResponse(responseCode = "200", description = "Country activated")
    public ResponseEntity<CountryResponse> activate(

            @Parameter(
                    name = "id",
                    description = "Country identifier",
                    required = true,
                    example = "901308db-4448-4b9e-a2f7-c0f899e10b6b")
            @PathVariable("id")
            UUID id) {

        return ResponseEntity.ok(
                applicationService.activate(
                        new ActivateCountryCommand(id)));
    }

    @Override
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a country")
    @ApiResponse(responseCode = "200", description = "Country deactivated")
    public ResponseEntity<CountryResponse> deactivate(

            @Parameter(
                    name = "id",
                    description = "Country identifier",
                    required = true,
                    example = "901308db-4448-4b9e-a2f7-c0f899e10b6b")
            @PathVariable("id")
            UUID id) {

        return ResponseEntity.ok(
                applicationService.deactivate(
                        new DeactivateCountryCommand(id)));
    }

    @Override
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a country")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Country deleted"),
            @ApiResponse(responseCode = "404", description = "Country not found", content = @Content)
    })
    public ResponseEntity<Void> delete(

            @Parameter(
                    name = "id",
                    description = "Country identifier",
                    required = true,
                    example = "901308db-4448-4b9e-a2f7-c0f899e10b6b")
            @PathVariable("id")
            UUID id) {

        applicationService.delete(
                new DeleteCountryCommand(id));

        return ResponseEntity.noContent().build();
    }

}