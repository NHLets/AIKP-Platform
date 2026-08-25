package org.afdb.aikp.modules.campaigncountry.presentation.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.afdb.aikp.modules.campaigncountry.application.command.AddCountryToCampaignCommand;
import org.afdb.aikp.modules.campaigncountry.application.command.RemoveCountryFromCampaignCommand;
import org.afdb.aikp.modules.campaigncountry.application.query.GetCampaignCountriesQuery;
import org.afdb.aikp.modules.campaigncountry.application.query.GetCampaignCountryQuery;
import org.afdb.aikp.modules.campaigncountry.application.response.CampaignCountryResponse;
import org.afdb.aikp.modules.campaigncountry.application.service.CampaignCountryApplicationService;
import org.afdb.aikp.modules.campaigncountry.presentation.request.AddCountryToCampaignRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing countries
 * assigned to campaigns.
 */
@RestController
@RequestMapping("/api/v1/campaigns/{campaignId}/countries")
@Tag(
        name = "Campaign Countries",
        description = "Operations for managing countries participating in AIKP campaigns."
)
public class CampaignCountryController {

    private final CampaignCountryApplicationService applicationService;

    public CampaignCountryController(
            CampaignCountryApplicationService applicationService) {

        this.applicationService = applicationService;
    }

    /**
     * Adds a country to a campaign.
     */
    @PostMapping
    @Operation(
            summary = "Add a country to a campaign",
            description = "Associates an existing country with an existing campaign."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Country successfully added to campaign"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Campaign or country not found"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Country already assigned to campaign"
            )
    })
    public ResponseEntity<CampaignCountryResponse> addCountry(
            @Parameter(
                    description = "Campaign UUID",
                    required = true
            )
            @PathVariable UUID campaignId,

            @Valid
            @RequestBody
            AddCountryToCampaignRequest request) {

        CampaignCountryResponse response =
                applicationService.addCountryToCampaign(
                        new AddCountryToCampaignCommand(
                                campaignId,
                                request.countryId()
                        )
                );

        URI location = URI.create(
                "/api/v1/campaigns/"
                        + campaignId
                        + "/countries/"
                        + request.countryId()
        );

        return ResponseEntity
                .created(location)
                .body(response);
    }

    /**
     * Retrieves all countries assigned to a campaign.
     */
    @GetMapping
    @Operation(
            summary = "Get countries assigned to a campaign",
            description = "Retrieves all country associations for the specified campaign."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Campaign countries retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Campaign not found"
            )
    })
    public ResponseEntity<List<CampaignCountryResponse>> getCountries(
            @Parameter(
                    description = "Campaign UUID",
                    required = true
            )
            @PathVariable UUID campaignId) {

        return ResponseEntity.ok(
                applicationService.getCampaignCountries(
                        new GetCampaignCountriesQuery(
                                campaignId
                        )
                )
        );
    }

    /**
     * Retrieves a specific country association
     * for a campaign.
     */
    @GetMapping("/{countryId}")
    @Operation(
            summary = "Get a campaign country association"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Campaign country association found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Campaign country association not found"
            )
    })
    public ResponseEntity<CampaignCountryResponse> getCountry(
            @Parameter(
                    description = "Campaign UUID",
                    required = true
            )
            @PathVariable UUID campaignId,

            @Parameter(
                    description = "Country UUID",
                    required = true
            )
            @PathVariable UUID countryId) {

        return ResponseEntity.ok(
                applicationService.getCampaignCountry(
                        new GetCampaignCountryQuery(
                                campaignId,
                                countryId
                        )
                )
        );
    }

    /**
     * Removes a country from a campaign.
     */
    @DeleteMapping("/{countryId}")
    @Operation(
            summary = "Remove a country from a campaign"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Country removed from campaign"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Campaign country association not found"
            )
    })
    public ResponseEntity<Void> removeCountry(
            @Parameter(
                    description = "Campaign UUID",
                    required = true
            )
            @PathVariable UUID campaignId,

            @Parameter(
                    description = "Country UUID",
                    required = true
            )
            @PathVariable UUID countryId) {

        applicationService.removeCountryFromCampaign(
                new RemoveCountryFromCampaignCommand(
                        campaignId,
                        countryId
                )
        );

        return ResponseEntity.noContent().build();
    }
}
