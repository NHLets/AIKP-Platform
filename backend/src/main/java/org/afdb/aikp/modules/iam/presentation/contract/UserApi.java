package org.afdb.aikp.modules.iam.presentation.contract;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.afdb.aikp.modules.iam.application.response.UserResponse;
import org.afdb.aikp.modules.iam.application.response.UserSummary;
import org.afdb.aikp.modules.iam.presentation.request.CreateUserRequest;
import org.afdb.aikp.modules.iam.presentation.request.UpdateUserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Users", description = "Identity and Access Management - User API")
@RequestMapping("/api/v1/users")
public interface UserApi {

    @Operation(summary = "Create a new user")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UserResponse create(
            @Valid @RequestBody CreateUserRequest request
    );

    @Operation(summary = "Get a user by id")
    @GetMapping("/{id}")
    UserResponse findById(
            @PathVariable UUID id
    );

    @Operation(summary = "Get all users")
    @GetMapping
    List<UserSummary> findAll();

    @Operation(summary = "Get all active users")
    @GetMapping("/active")
    List<UserSummary> findActive();

    @Operation(summary = "Update a user")
    @PutMapping("/{id}")
    UserResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request
    );

    @Operation(summary = "Activate a user")
    @PatchMapping("/{id}/activate")
    UserResponse activate(
            @PathVariable UUID id
    );

    @Operation(summary = "Deactivate a user")
    @PatchMapping("/{id}/deactivate")
    UserResponse deactivate(
            @PathVariable UUID id
    );

    @Operation(summary = "Lock a user")
    @PatchMapping("/{id}/lock")
    UserResponse lock(
            @PathVariable UUID id
    );

    @Operation(summary = "Unlock a user")
    @PatchMapping("/{id}/unlock")
    UserResponse unlock(
            @PathVariable UUID id
    );

    @Operation(summary = "Suspend a user")
    @PatchMapping("/{id}/suspend")
    UserResponse suspend(
            @PathVariable UUID id
    );

    @Operation(summary = "Delete a user")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(
            @PathVariable UUID id
    );
}