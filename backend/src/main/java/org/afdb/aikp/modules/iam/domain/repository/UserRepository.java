package org.afdb.aikp.modules.iam.domain.repository;

import org.afdb.aikp.modules.iam.domain.model.User;
import org.afdb.aikp.modules.iam.domain.valueobject.Email;
import org.afdb.aikp.modules.iam.domain.valueobject.UserId;
import org.afdb.aikp.modules.iam.domain.valueobject.Username;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UserId id);

    Optional<User> findByUsername(Username username);

    Optional<User> findByEmail(Email email);

    List<User> findAll();

    boolean existsByUsername(Username username);

    boolean existsByEmail(Email email);

    void delete(UserId id);
}