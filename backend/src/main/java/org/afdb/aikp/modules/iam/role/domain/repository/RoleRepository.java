package org.afdb.aikp.modules.iam.role.domain.repository;

import org.afdb.aikp.modules.iam.role.domain.enums.RoleStatus;
import org.afdb.aikp.modules.iam.role.domain.model.Role;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleId;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleName;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {

    Role save(Role role);

    Optional<Role> findById(RoleId id);

    Optional<Role> findByName(RoleName name);

    List<Role> findAll();

    List<Role> findByStatus(RoleStatus status);

    boolean existsById(RoleId id);

    boolean existsByName(RoleName name);

    void delete(RoleId id);
}