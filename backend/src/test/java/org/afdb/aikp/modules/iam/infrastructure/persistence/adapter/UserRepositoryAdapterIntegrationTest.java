package org.afdb.aikp.modules.iam.infrastructure.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.afdb.aikp.modules.iam.domain.enums.UserStatus;
import org.afdb.aikp.modules.iam.domain.model.User;
import org.afdb.aikp.modules.iam.domain.repository.UserRepository;
import org.afdb.aikp.modules.iam.domain.valueobject.Email;
import org.afdb.aikp.modules.iam.domain.valueobject.FullName;
import org.afdb.aikp.modules.iam.domain.valueobject.PasswordHash;
import org.afdb.aikp.modules.iam.domain.valueobject.UserId;
import org.afdb.aikp.modules.iam.domain.valueobject.Username;
import org.afdb.aikp.modules.iam.infrastructure.persistence.repository.SpringDataUserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRepositoryAdapterIntegrationTest {

    private static final String PASSWORD_HASH =
            "$2a$10$123456789012345678901234567890123456789012345678901234";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpringDataUserRepository springDataUserRepository;

    @BeforeEach
    void setUp() {

        springDataUserRepository.deleteAll();
    }

    @AfterEach
    void cleanUp() {

        springDataUserRepository.deleteAll();
    }

    @Test
    void shouldSaveAndFindUserById() {

        User user =
                createUser(
                        "john.doe",
                        "john.doe@example.com",
                        "John Doe");

        User savedUser =
                userRepository.save(user);

        assertThat(savedUser)
                .isNotNull();

        assertThat(
                userRepository.findById(
                        savedUser.getId()))
                .isPresent()
                .hasValueSatisfying(foundUser -> {

                    assertThat(
                            foundUser.getId())
                            .isEqualTo(
                                    savedUser.getId());

                    assertThat(
                            foundUser.getUsername())
                            .isEqualTo(
                                    Username.of("john.doe"));

                    assertThat(
                            foundUser.getEmail())
                            .isEqualTo(
                                    Email.of(
                                            "john.doe@example.com"));

                    assertThat(
                            foundUser.getFullName())
                            .isEqualTo(
                                    FullName.of("John Doe"));

                    assertThat(
                            foundUser.getStatus())
                            .isEqualTo(
                                    UserStatus.PENDING);
                });
    }

    @Test
    void shouldFindUserByUsername() {

        User savedUser =
                userRepository.save(
                        createUser(
                                "john.doe",
                                "john.doe@example.com",
                                "John Doe"));

        assertThat(
                userRepository.findByUsername(
                        Username.of("john.doe")))
                .isPresent()
                .hasValueSatisfying(foundUser -> {

                    assertThat(
                            foundUser.getId())
                            .isEqualTo(
                                    savedUser.getId());

                    assertThat(
                            foundUser.getUsername())
                            .isEqualTo(
                                    Username.of("john.doe"));
                });
    }

    @Test
    void shouldFindUserByEmail() {

        User savedUser =
                userRepository.save(
                        createUser(
                                "john.doe",
                                "john.doe@example.com",
                                "John Doe"));

        assertThat(
                userRepository.findByEmail(
                        Email.of(
                                "john.doe@example.com")))
                .isPresent()
                .hasValueSatisfying(foundUser -> {

                    assertThat(
                            foundUser.getId())
                            .isEqualTo(
                                    savedUser.getId());

                    assertThat(
                            foundUser.getEmail())
                            .isEqualTo(
                                    Email.of(
                                            "john.doe@example.com"));
                });
    }

    @Test
    void shouldFindAllUsers() {

        userRepository.save(
                createUser(
                        "john.doe",
                        "john.doe@example.com",
                        "John Doe"));

        userRepository.save(
                createUser(
                        "jane.doe",
                        "jane.doe@example.com",
                        "Jane Doe"));

        List<User> users =
                userRepository.findAll();

        assertThat(users)
                .hasSize(2)
                .extracting(
                        user -> user.getUsername().value())
                .containsExactlyInAnyOrder(
                        "john.doe",
                        "jane.doe");
    }

    @Test
    void shouldFindUsersByStatus() {

        User activeUser =
                createUser(
                        "john.doe",
                        "john.doe@example.com",
                        "John Doe");

        activeUser.activate();

        User pendingUser =
                createUser(
                        "jane.doe",
                        "jane.doe@example.com",
                        "Jane Doe");

        userRepository.save(activeUser);
        userRepository.save(pendingUser);

        List<User> users =
                userRepository.findByStatus(
                        UserStatus.ACTIVE);

        assertThat(users)
                .hasSize(1)
                .allSatisfy(user ->
                        assertThat(
                                user.getStatus())
                                .isEqualTo(
                                        UserStatus.ACTIVE));
    }

    @Test
    void shouldCheckIfUserExistsById() {

        User savedUser =
                userRepository.save(
                        createUser(
                                "john.doe",
                                "john.doe@example.com",
                                "John Doe"));

        boolean exists =
                userRepository.existsById(
                        savedUser.getId());

        assertThat(exists)
                .isTrue();
    }

    @Test
    void shouldCheckIfUserExistsByUsername() {

        userRepository.save(
                createUser(
                        "john.doe",
                        "john.doe@example.com",
                        "John Doe"));

        boolean exists =
                userRepository.existsByUsername(
                        Username.of("john.doe"));

        assertThat(exists)
                .isTrue();
    }

    @Test
    void shouldCheckIfUserExistsByEmail() {

        userRepository.save(
                createUser(
                        "john.doe",
                        "john.doe@example.com",
                        "John Doe"));

        boolean exists =
                userRepository.existsByEmail(
                        Email.of(
                                "john.doe@example.com"));

        assertThat(exists)
                .isTrue();
    }

    @Test
    void shouldDeleteUser() {

        User savedUser =
                userRepository.save(
                        createUser(
                                "john.doe",
                                "john.doe@example.com",
                                "John Doe"));

        UserId userId =
                savedUser.getId();

        userRepository.delete(userId);

        assertThat(
                userRepository.findById(userId))
                .isEmpty();

        assertThat(
                userRepository.existsById(userId))
                .isFalse();
    }

    private User createUser(
            String username,
            String email,
            String fullName) {

        return User.create(
                Username.of(username),
                Email.of(email),
                FullName.of(fullName),
                PasswordHash.of(PASSWORD_HASH));
    }
}
