package org.afdb.aikp.modules.auth.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.afdb.aikp.modules.auth.application.dto.LoginRequestDto;
import org.afdb.aikp.modules.auth.application.dto.LoginResponseDto;
import org.afdb.aikp.modules.auth.application.service.AuthApplicationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthApplicationService service;

    @Operation(summary = "User login")
    @PostMapping("/login")
    public LoginResponseDto login(
            @Valid @RequestBody LoginRequestDto request) {

        return service.login(request);
    }

}
