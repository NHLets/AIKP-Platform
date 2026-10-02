package org.afdb.aikp.modules.passwordreset.infrastructure.persistence.repository;

import org.afdb.aikp.modules.passwordreset.domain.model.PasswordReset;
import org.afdb.aikp.modules.passwordreset.domain.repository.PasswordResetRepository;
import org.afdb.aikp.modules.passwordreset.domain.valueobject.ResetToken;
import org.afdb.aikp.modules.passwordreset.infrastructure.persistence.entity.PasswordResetEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PasswordResetRepositoryAdapter implements PasswordResetRepository {

    private final SpringDataPasswordResetRepository jpaRepository;

    public PasswordResetRepositoryAdapter(SpringDataPasswordResetRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public PasswordReset save(PasswordReset reset) {
        PasswordResetEntity entity = PasswordResetEntity.fromDomain(reset);
        return jpaRepository.save(entity).toDomain();
    }

    @Override
    public Optional<PasswordReset> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(PasswordResetEntity::toDomain);
    }

    @Override
    public Optional<PasswordReset> findByToken(ResetToken token) {
        return jpaRepository.findByToken(token.value())
                .map(PasswordResetEntity::toDomain);
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }
}
