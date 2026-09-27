package org.afdb.aikp.modules.invitation.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.afdb.aikp.modules.invitation.application.dto.CreateInvitationRequestDto;
import org.afdb.aikp.modules.invitation.application.dto.InvitationResponseDto;
import org.afdb.aikp.modules.invitation.application.service.InvitationApplicationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationApplicationService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public InvitationResponseDto create(
            @Valid @RequestBody CreateInvitationRequestDto request) {

        var token = service.createInvitation(request.userId());

        return new InvitationResponseDto(token.value());
    }
}
