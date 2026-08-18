package org.afdb.aikp.modules.organization.domain.service;

import org.afdb.aikp.modules.organization.domain.exception.OrganizationCodeAlreadyExistsException;
import org.afdb.aikp.modules.organization.domain.model.Organization;
import org.afdb.aikp.modules.organization.domain.repository.OrganizationRepository;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationCode;
import org.afdb.aikp.modules.organization.domain.valueobject.OrganizationId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * Default implementation of the Organization domain service.
 *
 * <p>
 * Responsible for enforcing business rules involving the Organization
 * repository that cannot naturally belong to the aggregate itself.
 * </p>
 */
@Service
@Transactional(readOnly = true)
public class DefaultOrganizationDomainService
        implements OrganizationDomainService {

    private final OrganizationRepository repository;

    public DefaultOrganizationDomainService(
            OrganizationRepository repository) {

        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public void validateCreation(
            OrganizationCode code) {

        Objects.requireNonNull(code);

        if (repository.existsByCode(code)) {
            throw new OrganizationCodeAlreadyExistsException(
                    code.getValue());
        }
    }

    @Override
    public void validateUpdate(
            OrganizationId organizationId,
            OrganizationCode code) {

        Objects.requireNonNull(organizationId);
        Objects.requireNonNull(code);

        repository.findByCode(code)
                .ifPresent(existing -> {

                    if (!existing.getOrganizationId()
                            .equals(organizationId)) {

                        throw new OrganizationCodeAlreadyExistsException(
                                code.getValue());
                    }
                });
    }
}
