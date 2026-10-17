package org.afdb.aikp.modules.validation.presentation.controller;

import org.afdb.aikp.modules.validation.application.dto.CreateValidationCommentDto;
import org.afdb.aikp.modules.validation.application.dto.ValidationCommentDto;
import org.afdb.aikp.modules.validation.application.dto.ObservationCommentCountDto;
import org.afdb.aikp.modules.validation.application.service.ValidationCommentApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.afdb.aikp.modules.iam.domain.repository.UserRepository;
import org.afdb.aikp.modules.iam.domain.valueobject.Email;

@RestController
@RequestMapping("/api/validation-comments")
public class ValidationCommentController {

    private final ValidationCommentApplicationService service;
    private final UserRepository userRepository;

    public ValidationCommentController(ValidationCommentApplicationService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ValidationCommentDto create(
            @RequestBody CreateValidationCommentDto request, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new org.springframework.security.access.AccessDeniedException("Authentication required");
        }
        var user = userRepository.findByEmail(Email.of(authentication.getName()))
                .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException(authentication.getName()));
        return service.create(new CreateValidationCommentDto(request.observationId(),
                user.getId().getValue(), request.comment(), request.severity()));
    }

    @GetMapping("/{id}")
    public ValidationCommentDto findById(
            @PathVariable UUID id) {

        return service.findById(id);
    }

    @GetMapping("/observation/{observationId}")
    public List<ValidationCommentDto> findByObservation(
            @PathVariable("observationId") UUID observationId) {

        return service.findByObservation(observationId);
    }


    @GetMapping("/counts/{dataCollectionId}")
    public List<ObservationCommentCountDto> getCounts(
        @PathVariable("dataCollectionId") UUID dataCollectionId
    ) {
        return service.getCounts(dataCollectionId);
    }

}
