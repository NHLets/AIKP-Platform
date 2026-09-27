package org.afdb.aikp.modules.passwordreset.presentation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.afdb.aikp.modules.passwordreset.application.dto.*;
import org.afdb.aikp.modules.passwordreset.application.service.PasswordResetApplicationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetApplicationService service;

    @PostMapping("/forgot-password")
    public void forgot(@Valid @RequestBody ForgotPasswordRequestDto request){
        service.forgotPassword(request.email());
    }

    @GetMapping("/reset-password/validate")
    public boolean validate(@RequestParam String token){
        return service.validateToken(token);
    }

    @PostMapping("/reset-password")
    public void reset(@Valid @RequestBody ResetPasswordRequestDto request){
        service.resetPassword(request.token(), request.password());
    }
}
