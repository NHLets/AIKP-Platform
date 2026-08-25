package org.afdb.aikp.modules.iam.application.service;

import org.afdb.aikp.modules.iam.application.command.CreateUserCommand;
import org.afdb.aikp.modules.iam.application.command.UpdateUserCommand;
import org.afdb.aikp.modules.iam.application.response.UserResponse;
import org.afdb.aikp.modules.iam.application.response.UserSummary;
import org.afdb.aikp.modules.iam.domain.enums.UserStatus;
import org.afdb.aikp.modules.iam.domain.exception.UserNotFoundException;
import org.afdb.aikp.modules.iam.domain.model.User;
import org.afdb.aikp.modules.iam.domain.repository.UserRepository;
import org.afdb.aikp.modules.iam.domain.service.UserDomainService;
import org.afdb.aikp.modules.iam.domain.valueobject.Email;
import org.afdb.aikp.modules.iam.domain.valueobject.FullName;
import org.afdb.aikp.modules.iam.domain.valueobject.PasswordHash;
import org.afdb.aikp.modules.iam.domain.valueobject.UserId;
import org.afdb.aikp.modules.iam.domain.valueobject.Username;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserApplicationServiceTest {

    private UserRepository userRepository;

    private UserDomainService userDomainService;

    private PasswordEncoder passwordEncoder;

    private UserApplicationService service;

    @BeforeEach
    void setUp() {

        userRepository = mock(UserRepository.class);

        userDomainService =
                new UserDomainService(userRepository);

        passwordEncoder =
                mock(PasswordEncoder.class);

        service = new UserApplicationService(
                userRepository,
                userDomainService,
                passwordEncoder);
    }

    @Test
    void shouldCreateUser() {

        String encodedPassword =
                validPasswordHash();

        when(passwordEncoder.encode("password123"))
                .thenReturn(encodedPassword);

        when(userRepository.existsByUsername(
                Username.of("john.doe")))
                .thenReturn(false);

        when(userRepository.existsByEmail(
                Email.of("john.doe@example.com")))
                .thenReturn(false);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        UserResponse response =
                service.create(
                        new CreateUserCommand(
                                "john.doe",
                                "john.doe@example.com",
                                "John Doe",
                                "password123"));

        assertThat(response.id())
                .isNotNull();

        assertThat(response.username())
                .isEqualTo("john.doe");

        assertThat(response.email())
                .isEqualTo("john.doe@example.com");

        assertThat(response.fullName())
                .isEqualTo("John Doe");

        assertThat(response.status())
                .isEqualTo(UserStatus.PENDING);

        verify(passwordEncoder)
                .encode("password123");

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository)
                .save(userCaptor.capture());

        assertThat(userCaptor.getValue()
                .getPasswordHash()
                .value())
                .isEqualTo(encodedPassword);
    }

    @Test
    void shouldFindUserById() {

        User user =
                createUser();

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        UserResponse response =
                service.findById(user.getId());

        assertThat(response.id())
                .isEqualTo(
                        user.getId().getValue());

        assertThat(response.username())
                .isEqualTo("john.doe");
    }

    @Test
    void shouldThrowWhenFindingUnknownUser() {

        UserId id =
                UserId.of(UUID.randomUUID());

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> service.findById(id))
                .isInstanceOf(
                        UserNotFoundException.class);
    }

    @Test
    void shouldFindAllUsers() {

        User first =
                createUser();

        User second =
                User.create(
                        Username.of("jane.doe"),
                        Email.of("jane.doe@example.com"),
                        FullName.of("Jane Doe"),
                        PasswordHash.of(
                                validPasswordHash()));

        when(userRepository.findAll())
                .thenReturn(
                        List.of(first, second));

        List<UserSummary> users =
                service.findAll();

        assertThat(users)
                .hasSize(2);

        assertThat(users)
                .extracting(UserSummary::username)
                .containsExactly(
                        "john.doe",
                        "jane.doe");
    }

    @Test
    void shouldFindActiveUsers() {

        User user =
                createUser();

        user.activate();

        when(userRepository.findByStatus(
                UserStatus.ACTIVE))
                .thenReturn(List.of(user));

        List<UserSummary> users =
                service.findActive();

        assertThat(users)
                .hasSize(1);

        assertThat(users.getFirst().status())
                .isEqualTo(UserStatus.ACTIVE);

        verify(userRepository)
                .findByStatus(
                        UserStatus.ACTIVE);
    }

    @Test
    void shouldUpdateUser() {

        User user =
                createUser();

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(userRepository.save(user))
                .thenReturn(user);

        UserResponse response =
                service.update(
                        new UpdateUserCommand(
                                user.getId().getValue(),
                                "updated@example.com",
                                "Updated User"));

        assertThat(response.email())
                .isEqualTo("updated@example.com");

        assertThat(response.fullName())
                .isEqualTo("Updated User");

        verify(userRepository)
                .save(user);
    }

    @Test
    void shouldThrowWhenUpdatingUnknownUser() {

        UUID id =
                UUID.randomUUID();

        UserId userId =
                UserId.of(id);

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> service.update(
                        new UpdateUserCommand(
                                id,
                                "updated@example.com",
                                "Updated User")))
                .isInstanceOf(
                        UserNotFoundException.class);
    }

    @Test
    void shouldDeleteUser() {

        User user =
                createUser();

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        service.delete(user.getId());

        verify(userRepository)
                .delete(user.getId());
    }

    @Test
    void shouldThrowWhenDeletingUnknownUser() {

        UserId id =
                UserId.of(UUID.randomUUID());

        when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> service.delete(id))
                .isInstanceOf(
                        UserNotFoundException.class);
    }

    @Test
    void shouldActivateUser() {

        User user =
                createUser();

        mockExistingUser(user);

        UserResponse response =
                service.activate(user.getId());

        assertThat(response.status())
                .isEqualTo(UserStatus.ACTIVE);

        verify(userRepository)
                .save(user);
    }

    @Test
    void shouldDeactivateUser() {

        User user =
                createUser();

        mockExistingUser(user);

        UserResponse response =
                service.deactivate(user.getId());

        assertThat(response.status())
                .isEqualTo(UserStatus.INACTIVE);

        verify(userRepository)
                .save(user);
    }

    @Test
    void shouldLockUser() {

        User user =
                createUser();

        mockExistingUser(user);

        UserResponse response =
                service.lock(user.getId());

        assertThat(response.status())
                .isEqualTo(UserStatus.LOCKED);

        verify(userRepository)
                .save(user);
    }

    @Test
    void shouldUnlockUser() {

        User user =
                createUser();

        user.lock();

        mockExistingUser(user);

        UserResponse response =
                service.unlock(user.getId());

        assertThat(response.status())
                .isEqualTo(UserStatus.ACTIVE);

        verify(userRepository)
                .save(user);
    }

    @Test
    void shouldSuspendUser() {

        User user =
                createUser();

        mockExistingUser(user);

        UserResponse response =
                service.suspend(user.getId());

        assertThat(response.status())
                .isEqualTo(UserStatus.SUSPENDED);

        verify(userRepository)
                .save(user);
    }

    private void mockExistingUser(User user) {

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(userRepository.save(user))
                .thenReturn(user);
    }

    private User createUser() {

        return User.create(
                Username.of("john.doe"),
                Email.of("john.doe@example.com"),
                FullName.of("John Doe"),
                PasswordHash.of(
                        validPasswordHash()));
    }

    private String validPasswordHash() {

        return "$2a$10$"
                + "abcdefghijklmnopqrstuvwxyz"
                + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789abcdefghijklmnop";
    }
}
