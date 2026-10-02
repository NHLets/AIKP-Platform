package org.afdb.aikp.modules.validation.application.service;

import org.afdb.aikp.modules.validation.application.dto.CreateValidationCommentDto;
import org.afdb.aikp.modules.validation.application.dto.ValidationCommentDto;
import org.afdb.aikp.modules.validation.application.dto.ObservationCommentCountDto;
import org.afdb.aikp.modules.validation.domain.model.ValidationComment;
import org.afdb.aikp.modules.validation.domain.repository.ValidationCommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ValidationCommentApplicationService {

    private final ValidationCommentRepository repository;

    public ValidationCommentApplicationService(
            ValidationCommentRepository repository) {

        this.repository = repository;
    }

    public ValidationCommentDto create(
            CreateValidationCommentDto dto) {

        ValidationComment comment =
                ValidationComment.create(
                        dto.observationId(),
                        dto.validatorId(),
                        dto.comment(),
                        dto.severity());

        ValidationComment saved =
                repository.save(comment);

        return toDto(saved);
    }

    public List<ValidationCommentDto> findByObservation(
            UUID observationId) {

        return repository.findByObservationId(observationId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public ValidationCommentDto findById(UUID id) {

        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow();
    }

    private ValidationCommentDto toDto(
            ValidationComment comment) {

        return new ValidationCommentDto(
                comment.getId(),
                comment.getObservationId(),
                comment.getValidatorId(),
                comment.getComment(),
                comment.getSeverity(),
                comment.getCreatedAt(),
                comment.getUpdatedAt());
    }


    public List<ObservationCommentCountDto> getCounts(
        UUID dataCollectionId
    ) {

        long total = repository.countByDataCollection(dataCollectionId);

        return List.of(
            new ObservationCommentCountDto(
                dataCollectionId,
                total
            )
        );
    }

}
