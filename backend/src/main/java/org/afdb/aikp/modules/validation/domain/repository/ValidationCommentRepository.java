package org.afdb.aikp.modules.validation.domain.repository;

import org.afdb.aikp.modules.validation.domain.model.ValidationComment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ValidationCommentRepository {

    ValidationComment save(ValidationComment comment);

    Optional<ValidationComment> findById(UUID id);

    List<ValidationComment> findByObservationId(UUID observationId);

    List<ValidationComment> findByValidatorId(UUID validatorId);

    boolean existsById(UUID id);

}
