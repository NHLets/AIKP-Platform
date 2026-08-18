package org.afdb.aikp.modules.iam.role.infrastructure.persistence.adapter;

import org.afdb.aikp.modules.iam.role.domain.enums.RoleStatus;
import org.afdb.aikp.modules.iam.role.domain.model.Role;
import org.afdb.aikp.modules.iam.role.domain.repository.RoleRepository;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleId;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleName;
import org.afdb.aikp.modules.iam.role.infrastructure.persistence.entity.RoleEntity;
import org.afdb.aikp.modules.iam.role.infrastructure.persistence.mapper.RolePersistenceMapper;
import org.afdb.aikp.modules.iam.role.infrastructure.persistence.repository.SpringDataRoleRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RoleRepositoryAdapter implements RoleRepository {

    private final SpringDataRoleRepository repository;

    public RoleRepositoryAdapter(
            SpringDataRoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Role save(Role role) {

        RoleEntity entity =
                RolePersistenceMapper.toEntity(role);

        RoleEntity saved =
                repository.save(entity);

        return RolePersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Role> findById(RoleId id) {

        return repository.findById(id.getValue())
                .map(RolePersistenceMapper::toDomain);
    }

    @Override
    public Optional<Role> findByName(RoleName name) {

        return repository.findByName(name.value())
                .map(RolePersistenceMapper::toDomain);
    }

    @Override
    public List<Role> findAll() {

        return repository.findAll()
                .stream()
                .map(RolePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Role> findByStatus(RoleStatus status) {

        return repository.findByStatus(status)
                .stream()
                .map(RolePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(RoleId id) {

        return repository.existsById(id.getValue());
    }

    @Override
    public boolean existsByName(RoleName name) {

        return repository.existsByName(name.value());
    }

    @Override
    public void delete(RoleId id) {

        repository.deleteById(id.getValue());
    }
}