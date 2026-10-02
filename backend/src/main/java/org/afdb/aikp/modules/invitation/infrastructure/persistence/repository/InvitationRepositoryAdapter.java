package org.afdb.aikp.modules.invitation.infrastructure.persistence.repository;

import org.afdb.aikp.modules.invitation.domain.model.Invitation;
import org.afdb.aikp.modules.invitation.domain.repository.InvitationRepository;
import org.afdb.aikp.modules.invitation.domain.valueobject.InvitationToken;
import org.afdb.aikp.modules.invitation.infrastructure.persistence.entity.InvitationEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class InvitationRepositoryAdapter implements InvitationRepository {

    private final SpringDataInvitationRepository jpaRepository;

    public InvitationRepositoryAdapter(SpringDataInvitationRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Invitation save(Invitation invitation) {
        InvitationEntity entity = InvitationEntity.fromDomain(invitation);
        return jpaRepository.save(entity).toDomain();
    }

    @Override
    public Optional<Invitation> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(InvitationEntity::toDomain);
    }

    @Override
    public Optional<Invitation> findByToken(InvitationToken token) {
        return jpaRepository.findByToken(token.value())
                .map(InvitationEntity::toDomain);
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }
}
