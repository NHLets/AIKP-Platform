package org.afdb.aikp.modules.iam.domain.service;

import org.afdb.aikp.modules.iam.domain.enums.UserStatus;
import org.afdb.aikp.modules.iam.domain.exception.EmailAlreadyExistsException;
import org.afdb.aikp.modules.iam.domain.exception.UsernameAlreadyExistsException;
import org.afdb.aikp.modules.iam.domain.model.User;
import org.afdb.aikp.modules.iam.domain.repository.UserRepository;
import org.afdb.aikp.modules.iam.domain.valueobject.Email;
import org.afdb.aikp.modules.iam.domain.valueobject.FullName;
import org.afdb.aikp.modules.iam.domain.valueobject.PasswordHash;
import org.afdb.aikp.modules.iam.domain.valueobject.Username;

import java.util.Objects;

public class UserDomainService {

    private final UserRepository repository;

    public UserDomainService(UserRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    public User createUser(
            Username username,
            Email email,
            FullName fullName,
            PasswordHash passwordHash) {

        if (repository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(username.value());
        }

        if (repository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email.value());
        }

        User user = User.create(
                username,
                email,
                fullName,
                passwordHash);

        return repository.save(user);
    }

    public void activate(User user) {
        Objects.requireNonNull(user);
        user.activate();
        repository.save(user);
    }

    public void deactivate(User user) {
        Objects.requireNonNull(user);
        user.deactivate();
        repository.save(user);
    }

    public void suspend(User user) {
        Objects.requireNonNull(user);
        user.suspend();
        repository.save(user);
    }

    public void lock(User user) {
        Objects.requireNonNull(user);
        user.lock();
        repository.save(user);
    }

    public void unlock(User user) {
        Objects.requireNonNull(user);
        user.unlock();
        repository.save(user);
    }
}