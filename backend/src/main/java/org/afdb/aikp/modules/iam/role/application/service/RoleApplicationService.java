package org.afdb.aikp.modules.iam.role.application.service;

import org.afdb.aikp.modules.iam.role.application.command.ActivateRoleCommand;
import org.afdb.aikp.modules.iam.role.application.command.CreateRoleCommand;
import org.afdb.aikp.modules.iam.role.application.command.DeactivateRoleCommand;
import org.afdb.aikp.modules.iam.role.application.command.DeleteRoleCommand;
import org.afdb.aikp.modules.iam.role.application.command.UpdateRoleCommand;
import org.afdb.aikp.modules.iam.role.application.mapper.RoleApplicationMapper;
import org.afdb.aikp.modules.iam.role.application.query.GetActiveRolesQuery;
import org.afdb.aikp.modules.iam.role.application.query.GetRoleQuery;
import org.afdb.aikp.modules.iam.role.application.query.GetRolesQuery;
import org.afdb.aikp.modules.iam.role.application.response.RoleResponse;
import org.afdb.aikp.modules.iam.role.application.response.RoleSummary;
import org.afdb.aikp.modules.iam.role.domain.enums.RoleStatus;
import org.afdb.aikp.modules.iam.role.domain.exception.RoleNotFoundException;
import org.afdb.aikp.modules.iam.role.domain.model.Role;
import org.afdb.aikp.modules.iam.role.domain.repository.RoleRepository;
import org.afdb.aikp.modules.iam.role.domain.service.RoleDomainService;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Application service for the IAM Role aggregate.
 *
 * <p>
 * Coordinates application use cases while keeping business rules
 * inside the domain model and domain service.
 * </p>
 */
@Service
@Transactional
public class RoleApplicationService {

    private final RoleRepository roleRepository;
    private final RoleDomainService roleDomainService;

    public RoleApplicationService(
            RoleRepository roleRepository,
            RoleDomainService roleDomainService) {

        this.roleRepository =
                Objects.requireNonNull(roleRepository);

        this.roleDomainService =
                Objects.requireNonNull(roleDomainService);
    }

    /**
     * Creates a new Role.
     */
    public RoleResponse create(
            CreateRoleCommand command) {

        Objects.requireNonNull(command);

        roleDomainService.validateCreation(
                command.name());

        Role role = Role.create(
                command.name(),
                command.description(),
                command.system());

        Role saved = roleRepository.save(role);

        return RoleApplicationMapper.toResponse(saved);
    }

    /**
     * Retrieves a Role by identifier.
     */
    @Transactional(readOnly = true)
    public RoleResponse get(
            GetRoleQuery query) {

        Objects.requireNonNull(query);

        Role role = getRequiredRole(
                query.roleId());

        return RoleApplicationMapper.toResponse(role);
    }

    /**
     * Retrieves all Roles.
     */
    @Transactional(readOnly = true)
    public List<RoleSummary> getAll(
            GetRolesQuery query) {

        Objects.requireNonNull(query);

        return roleRepository.findAll()
                .stream()
                .map(RoleApplicationMapper::toSummary)
                .toList();
    }

    /**
     * Retrieves all active Roles.
     */
    @Transactional(readOnly = true)
    public List<RoleSummary> getActive(
            GetActiveRolesQuery query) {

        Objects.requireNonNull(query);

        return roleRepository.findByStatus(
                        RoleStatus.ACTIVE)
                .stream()
                .map(RoleApplicationMapper::toSummary)
                .toList();
    }

    /**
     * Updates an existing Role.
     */
    public RoleResponse update(
            UpdateRoleCommand command) {

        Objects.requireNonNull(command);

        Role role = getRequiredRole(
                command.roleId());

        roleDomainService.validateRename(
                role.getId(),
                command.name());

        role.rename(command.name());

        role.changeDescription(
                command.description());

        Role saved = roleRepository.save(role);

        return RoleApplicationMapper.toResponse(saved);
    }

    /**
     * Activates a Role.
     */
    public RoleResponse activate(
            ActivateRoleCommand command) {

        Objects.requireNonNull(command);

        Role role = getRequiredRole(
                command.roleId());

        role.activate();

        Role saved = roleRepository.save(role);

        return RoleApplicationMapper.toResponse(saved);
    }

    /**
     * Deactivates a Role.
     */
    public RoleResponse deactivate(
            DeactivateRoleCommand command) {

        Objects.requireNonNull(command);

        Role role = getRequiredRole(
                command.roleId());

        role.deactivate();

        Role saved = roleRepository.save(role);

        return RoleApplicationMapper.toResponse(saved);
    }

    /**
     * Deletes a Role.
     */
    public void delete(
            DeleteRoleCommand command) {

        Objects.requireNonNull(command);

        Role role = getRequiredRole(
                command.roleId());

        roleRepository.delete(
                role.getId());
    }

    /**
     * Retrieves a Role or throws a domain-specific
     * not-found exception.
     */
    private Role getRequiredRole(
            RoleId roleId) {

        return roleRepository.findById(roleId)
                .orElseThrow(
                        () -> RoleNotFoundException.withId(
                                roleId.getValue()));
    }
}