package org.afdb.aikp.modules.passwordreset.application.service;

import org.afdb.aikp.modules.iam.domain.repository.UserRepository;
import org.afdb.aikp.modules.iam.domain.valueobject.Email;
import org.afdb.aikp.modules.iam.domain.valueobject.UserId;
import org.afdb.aikp.modules.iam.domain.valueobject.PasswordHash;
import org.afdb.aikp.modules.passwordreset.domain.model.PasswordReset;
import org.afdb.aikp.modules.passwordreset.domain.repository.PasswordResetRepository;
import org.afdb.aikp.modules.passwordreset.domain.valueobject.PasswordResetId;
import org.afdb.aikp.modules.passwordreset.domain.valueobject.ResetToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class PasswordResetApplicationService {


    public PasswordResetApplicationService(
            PasswordResetRepository repository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    private final PasswordResetRepository repository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResetToken forgotPassword(String email) {

        var user = userRepository.findByEmail(Email.of(email))
                .orElseThrow();

        PasswordReset reset = new PasswordReset(
                PasswordResetId.generate(),
                user.getId().getValue(),
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

        var user = userRepository.findById(UserId.of(reset.getUserId()))
                .orElseThrow();

        user.changePassword(PasswordHash.of(passwordEncoder.encode(newPassword)));

        userRepository.save(user);

        reset.complete();
        repository.save(reset);
    }
}
