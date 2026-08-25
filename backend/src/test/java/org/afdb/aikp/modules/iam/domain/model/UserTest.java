package org.afdb.aikp.modules.iam.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.UUID;

import org.afdb.aikp.modules.iam.domain.enums.UserStatus;
import org.afdb.aikp.modules.iam.domain.exception.UserAlreadyActiveException;
import org.afdb.aikp.modules.iam.domain.exception.UserAlreadyLockedException;
import org.afdb.aikp.modules.iam.domain.valueobject.Email;
import org.afdb.aikp.modules.iam.domain.valueobject.FullName;
import org.afdb.aikp.modules.iam.domain.valueobject.PasswordHash;
import org.afdb.aikp.modules.iam.domain.valueobject.UserId;
import org.afdb.aikp.modules.iam.domain.valueobject.Username;

import org.junit.jupiter.api.Test;

class UserTest {

    private static final String PASSWORD_HASH =
            "$2a$10$123456789012345678901234567890123456789012345678901234567890";

    private static final String NEW_PASSWORD_HASH =
            "$2a$10$abcdefghijklmnopqrstuvwxyz123456789012345678901234567890";

    private static final String RESTORED_PASSWORD_HASH =
            "$2a$10$restored12345678901234567890123456789012345678901234567890";


    @Test
    void shouldCreatePendingUser() {

        User user =
                createUser();

        assertThat(user.getId())
                .isNotNull();

        assertThat(user.getUsername().value())
                .isEqualTo("john.doe");

        assertThat(user.getEmail().value())
                .isEqualTo("john.doe@example.com");

        assertThat(user.getFullName().value())
                .isEqualTo("John Doe");

        assertThat(user.getStatus())
                .isEqualTo(UserStatus.PENDING);

        assertThat(user.getCreatedAt())
                .isNotNull();

        assertThat(user.getUpdatedAt())
                .isNotNull();

        assertThat(user.getLastLogin())
                .isNull();
    }


    @Test
    void shouldActivateUser() {

        User user =
                createUser();

        user.activate();

        assertThat(user.getStatus())
                .isEqualTo(UserStatus.ACTIVE);
    }


    @Test
    void shouldThrowWhenActivatingAlreadyActiveUser() {

        User user =
                createUser();

        user.activate();

        assertThatThrownBy(
                user::activate)
                .isInstanceOf(
                        UserAlreadyActiveException.class);
    }


    @Test
    void shouldDeactivateUser() {

        User user =
                createUser();

        user.activate();
        user.deactivate();

        assertThat(user.getStatus())
                .isEqualTo(UserStatus.INACTIVE);
    }


    @Test
    void shouldDoNothingWhenDeactivatingInactiveUser() {

        User user =
                createUser();

        user.deactivate();

        assertThat(user.getStatus())
                .isEqualTo(UserStatus.INACTIVE);

        user.deactivate();

        assertThat(user.getStatus())
                .isEqualTo(UserStatus.INACTIVE);
    }


    @Test
    void shouldLockUser() {

        User user =
                createUser();

        user.lock();

        assertThat(user.getStatus())
                .isEqualTo(UserStatus.LOCKED);
    }


    @Test
    void shouldThrowWhenLockingAlreadyLockedUser() {

        User user =
                createUser();

        user.lock();

        assertThatThrownBy(
                user::lock)
                .isInstanceOf(
                        UserAlreadyLockedException.class);
    }


    @Test
    void shouldUnlockLockedUser() {

        User user =
                createUser();

        user.lock();
        user.unlock();

        assertThat(user.getStatus())
                .isEqualTo(UserStatus.ACTIVE);
    }


    @Test
    void shouldDoNothingWhenUnlockingNonLockedUser() {

        User user =
                createUser();

        user.unlock();

        assertThat(user.getStatus())
                .isEqualTo(UserStatus.PENDING);
    }


    @Test
    void shouldSuspendUser() {

        User user =
                createUser();

        user.suspend();

        assertThat(user.getStatus())
                .isEqualTo(UserStatus.SUSPENDED);
    }


    @Test
    void shouldDoNothingWhenSuspendingAlreadySuspendedUser() {

        User user =
                createUser();

        user.suspend();
        user.suspend();

        assertThat(user.getStatus())
                .isEqualTo(UserStatus.SUSPENDED);
    }


    @Test
    void shouldChangePassword() {

        User user =
                createUser();

        PasswordHash newPassword =
                PasswordHash.of(
                        NEW_PASSWORD_HASH);

        user.changePassword(
                newPassword);

        assertThat(user.getPasswordHash())
                .isEqualTo(
                        newPassword);
    }


    @Test
    void shouldChangeFullName() {

        User user =
                createUser();

        FullName newFullName =
                FullName.of(
                        "Jane Doe");

        user.changeFullName(
                newFullName);

        assertThat(user.getFullName())
                .isEqualTo(
                        newFullName);
    }


    @Test
    void shouldChangeEmail() {

        User user =
                createUser();

        Email newEmail =
                Email.of(
                        "jane.doe@example.com");

        user.changeEmail(
                newEmail);

        assertThat(user.getEmail())
                .isEqualTo(
                        newEmail);
    }


    @Test
    void shouldRecordSuccessfulLogin() {

        User user =
                createUser();

        assertThat(user.getLastLogin())
                .isNull();

        user.recordSuccessfulLogin();

        assertThat(user.getLastLogin())
                .isNotNull();
    }


    @Test
    void shouldRestoreUser() {

        UserId id =
                UserId.of(
                        UUID.randomUUID());

        Instant createdAt =
                Instant.parse(
                        "2026-01-01T10:00:00Z");

        Instant updatedAt =
                Instant.parse(
                        "2026-01-02T10:00:00Z");

        Instant lastLogin =
                Instant.parse(
                        "2026-01-03T10:00:00Z");

        User user =
                User.restore(
                        id,
                        Username.of(
                                "restored.user"),
                        Email.of(
                                "restored@example.com"),
                        FullName.of(
                                "Restored User"),
                        PasswordHash.of(
                                RESTORED_PASSWORD_HASH),
                        UserStatus.ACTIVE,
                        createdAt,
                        updatedAt,
                        lastLogin);

        assertThat(user.getId())
                .isEqualTo(id);

        assertThat(user.getUsername().value())
                .isEqualTo(
                        "restored.user");

        assertThat(user.getEmail().value())
                .isEqualTo(
                        "restored@example.com");

        assertThat(user.getFullName().value())
                .isEqualTo(
                        "Restored User");

        assertThat(user.getStatus())
                .isEqualTo(
                        UserStatus.ACTIVE);

        assertThat(user.getCreatedAt())
                .isEqualTo(
                        createdAt);

        assertThat(user.getUpdatedAt())
                .isEqualTo(
                        updatedAt);

        assertThat(user.getLastLogin())
                .isEqualTo(
                        lastLogin);
    }


    private User createUser() {

        return User.create(
                Username.of(
                        "john.doe"),
                Email.of(
                        "john.doe@example.com"),
                FullName.of(
                        "John Doe"),
                PasswordHash.of(
                        PASSWORD_HASH));
    }
}
