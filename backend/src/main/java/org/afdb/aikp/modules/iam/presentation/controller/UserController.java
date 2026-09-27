package org.afdb.aikp.modules.iam.presentation.controller;

import jakarta.validation.Valid;
import org.afdb.aikp.modules.iam.application.response.UserResponse;
import org.afdb.aikp.modules.iam.application.response.UserSummary;
import org.afdb.aikp.modules.iam.application.service.UserApplicationService;
import org.afdb.aikp.modules.iam.domain.valueobject.UserId;
import org.afdb.aikp.modules.iam.presentation.mapper.UserRestMapper;
import org.afdb.aikp.modules.iam.presentation.request.CreateUserRequest;
import org.afdb.aikp.modules.iam.presentation.request.UpdateUserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;
import org.afdb.aikp.modules.iam.application.dto.AssignRoleRequestDto;

@RestController
@PreAuthorize(\"hasRole('ADMIN')\")
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserApplicationService applicationService;

    public UserController(UserApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(
            @Valid @RequestBody CreateUserRequest request) {

        return applicationService.create(
                UserRestMapper.toCommand(request)
        );
    }

    @GetMapping
    public List<UserSummary> findAll() {
        return applicationService.findAll();
    }
    

    @GetMapping("/{id}")
    public UserResponse findById(
            @PathVariable UUID id) {

        return applicationService.findById(
        UserId.of(id)
        );
    }

    @PutMapping("/{id}")
    public UserResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request) {

        return applicationService.update(
                UserRestMapper.toCommand(id, request)
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID id) {

        applicationService.delete(
        UserId.of(id)
        );
    }


    @PostMapping("/{id}/activate")
    public void activate(@PathVariable UUID id) {
        service.activate(id);
    }

    @PostMapping("/{id}/deactivate")
    public void deactivate(@PathVariable UUID id) {
        service.deactivate(id);
    }

    @PostMapping("/{id}/lock")
    public void lock(@PathVariable UUID id) {
        service.lock(id);
    }

    @PostMapping("/{id}/unlock")
    public void unlock(@PathVariable UUID id) {
        service.unlock(id);
    }

    @PostMapping("/{id}/suspend")
    public void suspend(@PathVariable UUID id) {
        service.suspend(id);
    }



    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public void assignRole(
            @PathVariable UUID id,
            @Valid @RequestBody AssignRoleRequestDto request) {

        service.assignRole(id, request.roleId());
    }

}