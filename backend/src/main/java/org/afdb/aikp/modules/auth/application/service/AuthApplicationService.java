package org.afdb.aikp.modules.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.afdb.aikp.modules.auth.application.dto.LoginRequestDto;
import org.afdb.aikp.modules.auth.application.dto.LoginResponseDto;
import org.afdb.aikp.modules.iam.domain.model.User;
import org.afdb.aikp.modules.iam.domain.repository.UserRepository;
import org.afdb.aikp.shared.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthApplicationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash().value())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtService.generateToken(
                user.getId().value(),
                user.getEmail().value(),
                "USER");

        return new LoginResponseDto(
                token,
                "Bearer",
                user.getId().value(),
                user.getEmail().value(),
                "USER");
    }

}
