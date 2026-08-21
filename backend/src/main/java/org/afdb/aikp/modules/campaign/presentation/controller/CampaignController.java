package org.afdb.aikp.modules.campaign.presentation.controller;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.afdb.aikp.modules.campaign.application.command.ActivateCampaignCommand;
import org.afdb.aikp.modules.campaign.application.command.ArchiveCampaignCommand;
import org.afdb.aikp.modules.campaign.application.command.CompleteCampaignCommand;
import org.afdb.aikp.modules.campaign.application.command.DeleteCampaignCommand;
import org.afdb.aikp.modules.campaign.application.command.PlanCampaignCommand;
import org.afdb.aikp.modules.campaign.application.response.CampaignResponse;
import org.afdb.aikp.modules.campaign.application.service.CampaignApplicationService;
import org.afdb.aikp.modules.campaign.application.query.GetCampaignQuery;
import org.afdb.aikp.modules.campaign.presentation.mapper.CampaignRestMapper;
import org.afdb.aikp.modules.campaign.presentation.request.CreateCampaignRequest;
import org.afdb.aikp.modules.campaign.presentation.request.UpdateCampaignRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.afdb.aikp.modules.campaign.application.query.GetCampaignsQuery;
import org.afdb.aikp.modules.campaign.application.response.CampaignSummary;

import java.util.List;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/campaigns")
@Tag(
        name = "Campaigns",
        description = "AIKP data collection campaign management"
)
public class CampaignController {

    private final CampaignApplicationService service;

    public CampaignController(
            CampaignApplicationService service) {

        this.service = service;
    }

    @PostMapping
    @Operation(
            summary = "Create a campaign",
            description = "Creates a new AIKP data collection campaign."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Campaign created"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Campaign code already exists"
            )
    })
    public ResponseEntity<CampaignResponse> create(
            @Valid @RequestBody CreateCampaignRequest request) {

        CampaignResponse response =
                service.create(
                        CampaignRestMapper.toCommand(request));

        URI location = URI.create(
                "/api/v1/campaigns/" + response.id());

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @GetMapping
    @Operation(
        summary = "Get all campaigns",
        description = "Retrieves all AIKP data collection campaigns."
        )
    @ApiResponse(
        responseCode = "200",
        description = "Campaigns retrieved successfully"
     )
    public ResponseEntity<List<CampaignSummary>> getAll() {

    return ResponseEntity.ok(
            service.getAll(
                    new GetCampaignsQuery()));
}
    @GetMapping("/{id}")
    @Operation(
            summary = "Get campaign by ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Campaign found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Campaign not found"
            )
    })
    public ResponseEntity<CampaignResponse> getById(
            @Parameter(description = "Campaign UUID")
            @PathVariable("id") UUID id) {

        return ResponseEntity.ok(
                service.getById(
                        new GetCampaignQuery(id)));
    }

    @GetMapping("/code/{code}")
    @Operation(
            summary = "Get campaign by business code"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Campaign found"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Campaign not found"
            )
    })
    public ResponseEntity<CampaignResponse> getByCode(
            @Parameter(description = "Campaign business code")
            @PathVariable("code") String code) {

        return ResponseEntity.ok(
                service.getByCode(code));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a campaign"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Campaign updated"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Campaign not found"
            )
    })
    public ResponseEntity<CampaignResponse> update(
            @Parameter(description = "Campaign UUID")
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateCampaignRequest request) {

        return ResponseEntity.ok(
                service.update(
                        CampaignRestMapper.toCommand(id, request)));
    }

    @PatchMapping("/{id}/plan")
    @Operation(
            summary = "Plan a campaign"
    )
    public ResponseEntity<CampaignResponse> plan(
            @PathVariable("id") UUID id) {

        return ResponseEntity.ok(
                service.plan(
                        new PlanCampaignCommand(id)));
    }

    @PatchMapping("/{id}/activate")
    @Operation(
            summary = "Activate a campaign"
    )
    public ResponseEntity<CampaignResponse> activate(
            @PathVariable("id") UUID id) {

        return ResponseEntity.ok(
                service.activate(
                        new ActivateCampaignCommand(id)));
    }

    @PatchMapping("/{id}/complete")
    @Operation(
            summary = "Complete a campaign"
    )
    public ResponseEntity<CampaignResponse> complete(
            @PathVariable("id") UUID id) {

        return ResponseEntity.ok(
                service.complete(
                        new CompleteCampaignCommand(id)));
    }

    @PatchMapping("/{id}/archive")
    @Operation(
            summary = "Archive a campaign"
    )
    public ResponseEntity<CampaignResponse> archive(
            @PathVariable("id") UUID id) {

        return ResponseEntity.ok(
                service.archive(
                        new ArchiveCampaignCommand(id)));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a campaign"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Campaign deleted"
    )
    public ResponseEntity<Void> delete(
            @PathVariable("id") UUID id) {

        service.delete(
                new DeleteCampaignCommand(id));

        return ResponseEntity.noContent().build();
    }
}
