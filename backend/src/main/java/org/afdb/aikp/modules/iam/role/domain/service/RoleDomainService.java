package org.afdb.aikp.modules.iam.role.domain.service;

import org.afdb.aikp.modules.iam.role.domain.exception.RoleAlreadyExistsException;
import org.afdb.aikp.modules.iam.role.domain.model.Role;
import org.afdb.aikp.modules.iam.role.domain.repository.RoleRepository;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleId;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleName;

import java.util.Objects;

/**
 * Domain service responsible for business rules concerning IAM roles.
 */

public class RoleDomainService {

    private final RoleRepository repository;

    public RoleDomainService(RoleRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    /**
     * Validates that a role can be created with the given name.
     */
    public void validateCreation(RoleName name) {

        Objects.requireNonNull(name);

        if (repository.existsByName(name)) {
            throw RoleAlreadyExistsException.withName(
                    name.value());
        }
    }

    /**
     * Validates that an existing role can be renamed.
     */
    public void validateRename(
            RoleId roleId,
            RoleName name) {

        Objects.requireNonNull(roleId);
        Objects.requireNonNull(name);

        repository.findByName(name)
                .ifPresent(existing -> {

                    if (!existing.getId().equals(roleId)) {
                        throw RoleAlreadyExistsException.withName(
                                name.value());
                    }
                });
    }
}