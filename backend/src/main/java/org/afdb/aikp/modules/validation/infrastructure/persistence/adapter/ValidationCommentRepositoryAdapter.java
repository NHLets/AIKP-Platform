package org.afdb.aikp.modules.validation.infrastructure.persistence.adapter;

import org.afdb.aikp.modules.validation.domain.model.ValidationComment;
import org.afdb.aikp.modules.validation.domain.repository.ValidationCommentRepository;
import org.afdb.aikp.modules.validation.infrastructure.persistence.mapper.ValidationCommentMapper;
import org.afdb.aikp.modules.validation.infrastructure.persistence.repository.ValidationCommentJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ValidationCommentRepositoryAdapter
        implements ValidationCommentRepository {

    private final ValidationCommentJpaRepository repository;
    private final ValidationCommentMapper mapper;

    public ValidationCommentRepositoryAdapter(
            ValidationCommentJpaRepository repository,
            ValidationCommentMapper mapper) {

        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ValidationComment save(ValidationComment comment) {

        return mapper.toDomain(
                repository.save(
                        mapper.toEntity(comment)));
    }

    @Override
    public Optional<ValidationComment> findById(UUID id) {

        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<ValidationComment> findByObservationId(UUID observationId) {

        return repository
                .findByObservationIdOrderByCreatedAtAsc(observationId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<ValidationComment> findByValidatorId(UUID validatorId) {

        return repository
                .findByValidatorIdOrderByCreatedAtDesc(validatorId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(UUID id) {

        return repository.existsById(id);
    }
}
