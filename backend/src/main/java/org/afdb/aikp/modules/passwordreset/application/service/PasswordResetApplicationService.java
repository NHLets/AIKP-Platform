package org.afdb.aikp.modules.passwordreset.application.service;

import lombok.RequiredArgsConstructor;
import org.afdb.aikp.modules.iam.domain.repository.UserRepository;
import org.afdb.aikp.modules.passwordreset.domain.model.PasswordReset;
import org.afdb.aikp.modules.passwordreset.domain.repository.PasswordResetRepository;
import org.afdb.aikp.modules.passwordreset.domain.valueobject.PasswordResetId;
import org.afdb.aikp.modules.passwordreset.domain.valueobject.ResetToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class PasswordResetApplicationService {

    private final PasswordResetRepository repository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResetToken forgotPassword(String email) {

        var user = userRepository.findByEmail(email)
                .orElseThrow();

        PasswordReset reset = new PasswordReset(
                PasswordResetId.generate(),
                user.getId().value(),
                ResetToken.generate(),
                Instant.now().plus(30, ChronoUnit.MINUTES),
                Instant.now()
        );

        repository.save(reset);

        return reset.getToken();
    }

    public boolean validateToken(String token) {

        return repository.findByToken(new ResetToken(token))
                .filter(r -> r.getStatus().name().equals("PENDING"))
                .filter(r -> r.getExpiresAt().isAfter(Instant.now()))
                .isPresent();
    }

    public void resetPassword(String token, String newPassword) {

        PasswordReset reset = repository.findByToken(new ResetToken(token))
                .orElseThrow();

        if (reset.getExpiresAt().isBefore(Instant.now())) {
            reset.expire();
            repository.save(reset);
            throw new IllegalStateException("Token expired");
        }

        var user = userRepository.findById(reset.getUserId())
                .orElseThrow();

        user.changePassword(passwordEncoder.encode(newPassword));

        repository.save(user);

        reset.complete();
        repository.save(reset);
    }
}\n