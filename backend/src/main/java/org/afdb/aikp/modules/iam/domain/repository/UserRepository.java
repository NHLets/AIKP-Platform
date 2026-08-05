package org.afdb.aikp.modules.iam.domain.repository;

import org.afdb.aikp.modules.iam.domain.enums.UserStatus;
import org.afdb.aikp.modules.iam.domain.model.User;
import org.afdb.aikp.modules.iam.domain.valueobject.Email;
import org.afdb.aikp.modules.iam.domain.valueobject.UserId;
import org.afdb.aikp.modules.iam.domain.valueobject.Username;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    /**
     * Persists a user.
     */
    User save(User user);

    /**
     * Finds a user by its identifier.
     */
    Optional<User> findById(UserId id);

    /**
     * Finds a user by username.
     */
    Optional<User> findByUsername(Username username);

    /**
     * Finds a user by email.
     */
    Optional<User> findByEmail(Email email);

    /**
     * Returns all users.
     */
    List<User> findAll();

    /**
     * Returns all users having the specified status.
     */
    List<User> findByStatus(UserStatus status);

    /**
     * Checks whether a user exists with the given identifier.
     */
    boolean existsById(UserId id);

    /**
     * Checks whether a username already exists.
     */
    boolean existsByUsername(Username username);

    /**
     * Checks whether an email already exists.
     */
    boolean existsByEmail(Email email);

    /**
     * Deletes a user.
     */
    void delete(UserId id);
}