package org.afdb.aikp.modules.iam.role.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.afdb.aikp.modules.iam.role.application.command.ActivateRoleCommand;
import org.afdb.aikp.modules.iam.role.application.command.DeactivateRoleCommand;
import org.afdb.aikp.modules.iam.role.application.command.DeleteRoleCommand;
import org.afdb.aikp.modules.iam.role.application.query.GetActiveRolesQuery;
import org.afdb.aikp.modules.iam.role.application.query.GetRoleQuery;
import org.afdb.aikp.modules.iam.role.application.query.GetRolesQuery;
import org.afdb.aikp.modules.iam.role.application.response.RoleResponse;
import org.afdb.aikp.modules.iam.role.application.response.RoleSummary;
import org.afdb.aikp.modules.iam.role.application.service.RoleApplicationService;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleId;
import org.afdb.aikp.modules.iam.role.presentation.contract.RoleApi;
import org.afdb.aikp.modules.iam.role.presentation.mapper.RoleRestMapper;
import org.afdb.aikp.modules.iam.role.presentation.request.CreateRoleRequest;
import org.afdb.aikp.modules.iam.role.presentation.request.UpdateRoleRequest;
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

/**
 * REST controller for IAM Role operations.
 */
@RestController
@RequestMapping("/api/v1/roles")
@Tag(
        name = "Role",
        description = "Operations for managing IAM roles."
)
public class RoleController implements RoleApi {

    private final RoleApplicationService applicationService;

    public RoleController(
            RoleApplicationService applicationService) {

        this.applicationService = applicationService;
    }

    @Override
    @PostMapping
    @Operation(summary = "Create a new role")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Role created"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request",
                    content = @Content),
            @ApiResponse(
                    responseCode = "409",
                    description = "Role name already exists",
                    content = @Content)
    })
    public ResponseEntity<RoleResponse> create(
            @Valid
            @RequestBody
            CreateRoleRequest request) {

        RoleResponse response =
                applicationService.create(
                        RoleRestMapper.toCommand(request));

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
    @Operation(summary = "Retrieve a role by its identifier")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Role found"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Role not found",
                    content = @Content)
    })
    public ResponseEntity<RoleResponse> get(

            @Parameter(
                    name = "id",
                    description = "Role identifier",
                    required = true,
                    example = "901308db-4448-4b9e-a2f7-c0f899e10b6b")
            @PathVariable("id")
            UUID id) {

        return ResponseEntity.ok(
                applicationService.get(
                        new GetRoleQuery(
                                RoleId.of(id))));
    }

    @Override
    @GetMapping
    @Operation(summary = "Retrieve all roles")
    @ApiResponse(
            responseCode = "200",
            description = "Roles retrieved")
    public ResponseEntity<List<RoleSummary>> getAll() {

        return ResponseEntity.ok(
                applicationService.getAll(
                        new GetRolesQuery()));
    }

    @Override
    @GetMapping("/active")
    @Operation(summary = "Retrieve active roles")
    @ApiResponse(
            responseCode = "200",
            description = "Active roles retrieved")
    public ResponseEntity<List<RoleSummary>> getActive() {

        return ResponseEntity.ok(
                applicationService.getActive(
                        new GetActiveRolesQuery()));
    }

    @Override
    @PutMapping("/{id}")
    @Operation(summary = "Update a role")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Role updated"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Role not found",
                    content = @Content),
            @ApiResponse(
                    responseCode = "409",
                    description = "Role name already exists",
                    content = @Content)
    })
    public ResponseEntity<RoleResponse> update(

            @Parameter(
                    name = "id",
                    description = "Role identifier",
                    required = true,
                    example = "901308db-4448-4b9e-a2f7-c0f899e10b6b")
            @PathVariable("id")
            UUID id,

            @Valid
            @RequestBody
            UpdateRoleRequest request) {

        return ResponseEntity.ok(
                applicationService.update(
                        RoleRestMapper.toCommand(
                                id,
                                request)));
    }

    @Override
    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate a role")
    @ApiResponse(
            responseCode = "200",
            description = "Role activated")
    public ResponseEntity<RoleResponse> activate(

            @Parameter(
                    name = "id",
                    description = "Role identifier",
                    required = true,
                    example = "901308db-4448-4b9e-a2f7-c0f899e10b6b")
            @PathVariable("id")
            UUID id) {

        return ResponseEntity.ok(
                applicationService.activate(
                        new ActivateRoleCommand(
                                RoleId.of(id))));
    }

    @Override
    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate a role")
    @ApiResponse(
            responseCode = "200",
            description = "Role deactivated")
    public ResponseEntity<RoleResponse> deactivate(

            @Parameter(
                    name = "id",
                    description = "Role identifier",
                    required = true,
                    example = "901308db-4448-4b9e-a2f7-c0f899e10b6b")
            @PathVariable("id")
            UUID id) {

        return ResponseEntity.ok(
                applicationService.deactivate(
                        new DeactivateRoleCommand(
                                RoleId.of(id))));
    }

    @Override
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a role")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Role deleted"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Role not found",
                    content = @Content)
    })
    public ResponseEntity<Void> delete(

            @Parameter(
                    name = "id",
                    description = "Role identifier",
                    required = true,
                    example = "901308db-4448-4b9e-a2f7-c0f899e10b6b")
            @PathVariable("id")
            UUID id) {

        applicationService.delete(
                new DeleteRoleCommand(
                        RoleId.of(id)));

        return ResponseEntity.noContent().build();
    }
}
