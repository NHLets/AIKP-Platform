package org.afdb.aikp.modules.auth.application.service;

import lombok.RequiredArgsConstructor;
import org.afdb.aikp.modules.auth.application.dto.LoginRequestDto;
import org.afdb.aikp.modules.auth.application.dto.LoginResponseDto;
import org.afdb.aikp.modules.iam.domain.model.User;
import org.afdb.aikp.modules.iam.domain.valueobject.Email;
import org.afdb.aikp.modules.iam.domain.repository.UserRepository;
import org.afdb.aikp.shared.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthApplicationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthApplicationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByEmail(Email.of(request.email()))
                .orElseThrow(() ->
                        new BadCredentialsException("Invalid credentials"));

        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash().value())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        if (user.getRole() == null) {
            throw new IllegalStateException(
                    "Authenticated user has no assigned role: "
                            + user.getEmail().value());
        }

        String role = user.getRole().getName().value();

        String token = jwtService.generateToken(
                user.getId().getValue(),
                user.getEmail().value(),
                java.util.List.of(role)
        );

        return new LoginResponseDto(
                token,
                "Bearer",
                user.getId().getValue().toString(),
                user.getEmail().value(),
                role);
    }

}
