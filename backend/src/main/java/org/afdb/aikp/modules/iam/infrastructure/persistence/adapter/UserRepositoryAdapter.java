package org.afdb.aikp.modules.iam.infrastructure.persistence.adapter;

import org.afdb.aikp.modules.iam.domain.enums.UserStatus;
import org.afdb.aikp.modules.iam.domain.model.User;
import org.afdb.aikp.modules.iam.domain.repository.UserRepository;
import org.afdb.aikp.modules.iam.domain.valueobject.Email;
import org.afdb.aikp.modules.iam.domain.valueobject.UserId;
import org.afdb.aikp.modules.iam.domain.valueobject.Username;
import org.afdb.aikp.modules.iam.infrastructure.persistence.entity.UserEntity;
import org.afdb.aikp.modules.iam.infrastructure.persistence.mapper.UserPersistenceMapper;
import org.afdb.aikp.modules.iam.infrastructure.persistence.repository.SpringDataUserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository repository;

    public UserRepositoryAdapter(SpringDataUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {

        UserEntity entity =
                UserPersistenceMapper.toEntity(user);

        UserEntity saved =
                repository.save(entity);

        return UserPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(UserId id) {

        return repository.findById(id.getValue())
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(Username username) {

        return repository.findByUsername(username.value())
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {

        return repository.findByEmail(email.value())
                .map(UserPersistenceMapper::toDomain);
    }

    @Override
    public List<User> findAll() {

        return repository.findAll()
                .stream()
                .map(UserPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<User> findByStatus(UserStatus status) {

        return repository.findByStatus(status)
                .stream()
                .map(UserPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(UserId id) {

        return repository.existsById(id.getValue());
    }

    @Override
    public boolean existsByUsername(Username username) {

        return repository.existsByUsername(username.value());
    }

    @Override
    public boolean existsByEmail(Email email) {

        return repository.existsByEmail(email.value());
    }

    @Override
    public void delete(UserId id) {

        repository.deleteById(id.getValue());
    }
}