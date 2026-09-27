package org.afdb.aikp.modules.passwordreset.domain.repository;

import org.afdb.aikp.modules.passwordreset.domain.model.PasswordReset;
import org.afdb.aikp.modules.passwordreset.domain.valueobject.ResetToken;

import java.util.Optional;
import java.util.UUID;

public interface PasswordResetRepository {

    PasswordReset save(PasswordReset reset);

    Optional<PasswordReset> findById(UUID id);

    Optional<PasswordReset> findByToken(ResetToken token);

    void delete(UUID id);
}\n