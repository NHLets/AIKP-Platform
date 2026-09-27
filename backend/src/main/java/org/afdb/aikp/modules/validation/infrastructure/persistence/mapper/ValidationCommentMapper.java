package org.afdb.aikp.modules.validation.infrastructure.persistence.mapper;

import org.afdb.aikp.modules.validation.domain.model.ValidationComment;
import org.afdb.aikp.modules.validation.infrastructure.persistence.entity.ValidationCommentEntity;
import org.springframework.stereotype.Component;

@Component
public class ValidationCommentMapper {

    public ValidationCommentEntity toEntity(ValidationComment domain) {

        ValidationCommentEntity entity = new ValidationCommentEntity();

        entity.setId(domain.getId());
        entity.setObservationId(domain.getObservationId());
        entity.setValidatorId(domain.getValidatorId());
        entity.setComment(domain.getComment());
        entity.setSeverity(domain.getSeverity());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }

    public ValidationComment toDomain(ValidationCommentEntity entity) {

        return new ValidationComment(
                entity.getId(),
                entity.getObservationId(),
                entity.getValidatorId(),
                entity.getComment(),
                entity.getSeverity(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
