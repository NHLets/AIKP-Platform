package org.afdb.aikp.modules.iam.application.service;

import org.afdb.aikp.modules.iam.application.command.CreateUserCommand;
import org.afdb.aikp.modules.invitation.domain.repository.InvitationRepository;
import org.afdb.aikp.modules.passwordreset.domain.repository.PasswordResetRepository;
import org.afdb.aikp.modules.iam.application.command.UpdateUserCommand;
import org.afdb.aikp.modules.iam.application.mapper.UserApplicationMapper;
import org.afdb.aikp.modules.iam.application.response.UserResponse;
import org.afdb.aikp.modules.iam.application.response.UserSummary;
import org.afdb.aikp.modules.iam.domain.enums.UserStatus;
import org.afdb.aikp.modules.iam.domain.exception.UserNotFoundException;
import org.afdb.aikp.modules.iam.domain.exception.UserInUseException;
import org.afdb.aikp.modules.validation.domain.repository.DataCollectionValidationRepository;
import org.afdb.aikp.modules.iam.domain.model.User;
import org.afdb.aikp.modules.iam.domain.repository.UserRepository;
import org.afdb.aikp.modules.iam.role.domain.repository.RoleRepository;
import org.afdb.aikp.modules.iam.domain.service.UserDomainService;
import org.afdb.aikp.modules.iam.domain.valueobject.Email;
import org.afdb.aikp.modules.iam.domain.valueobject.FullName;
import org.afdb.aikp.modules.iam.domain.valueobject.PasswordHash;
import org.afdb.aikp.modules.iam.domain.valueobject.UserId;
import org.afdb.aikp.modules.iam.role.domain.valueobject.RoleId;
import org.afdb.aikp.modules.iam.domain.valueobject.Username;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserApplicationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserDomainService userDomainService;
    private final PasswordEncoder passwordEncoder;
    private final InvitationRepository invitationRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final DataCollectionValidationRepository dataCollectionValidationRepository;

    public UserApplicationService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserDomainService userDomainService,
            PasswordEncoder passwordEncoder,
            InvitationRepository invitationRepository,
            PasswordResetRepository passwordResetRepository,
            DataCollectionValidationRepository dataCollectionValidationRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userDomainService = userDomainService;
        this.passwordEncoder = passwordEncoder;
        this.invitationRepository = invitationRepository;
        this.passwordResetRepository = passwordResetRepository;
        this.dataCollectionValidationRepository = dataCollectionValidationRepository;
    }


    public UserResponse create(CreateUserCommand command) {

        PasswordHash passwordHash =
                PasswordHash.of(passwordEncoder.encode(command.password()));

        var role = roleRepository.findById(
                RoleId.of(command.roleId())
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "Role not found: " + command.roleId()
                )
        );

        User user = userDomainService.createUser(
                Username.of(command.username()),
                Email.of(command.email()),
                FullName.of(command.fullName()),
                passwordHash,
                role
        );

        return UserApplicationMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse findById(UserId id) {

        return UserApplicationMapper.toResponse(loadUser(id));
    }

    @Transactional(readOnly = true)
    public List<UserSummary> findAll() {

        return UserApplicationMapper.toSummaryList(
                userRepository.findAll()
        );
    }

    @Transactional(readOnly = true)
    public List<UserSummary> findActive() {

        return UserApplicationMapper.toSummaryList(
                userRepository.findByStatus(UserStatus.ACTIVE)
        );
    }

    @Transactional
    public UserResponse update(UpdateUserCommand command) {

        User user = loadUser(
                UserId.of(command.id())
        );

        user.changeEmail(
                Email.of(command.email())
        );

        user.changeFullName(
                FullName.of(command.fullName())
        );

        User saved = userRepository.save(user);

        return UserApplicationMapper.toResponse(saved);
    }

    @Transactional
    public void delete(UserId id) {

        loadUser(id);

        UUID userUuid = id.getValue();

        if (dataCollectionValidationRepository
                .existsByValidatorUserId(userUuid)) {
            throw new UserInUseException(userUuid);
        }

        invitationRepository.deleteByUserId(userUuid);
        passwordResetRepository.deleteByUserId(userUuid);
        userRepository.delete(id);
    }

    @Transactional
    public UserResponse activate(UserId id) {

        User user = loadUser(id);

        user.activate();

        return UserApplicationMapper.toResponse(
                userRepository.save(user)
        );
    }

    @Transactional
    public UserResponse deactivate(UserId id) {

        User user = loadUser(id);

        user.deactivate();

        return UserApplicationMapper.toResponse(
                userRepository.save(user)
        );
    }

    @Transactional
    public UserResponse lock(UserId id) {

        User user = loadUser(id);

        user.lock();

        return UserApplicationMapper.toResponse(
                userRepository.save(user)
        );
    }

    @Transactional
    public UserResponse unlock(UserId id) {

        User user = loadUser(id);

        user.unlock();

        return UserApplicationMapper.toResponse(
                userRepository.save(user)
        );
    }

    @Transactional
    public UserResponse suspend(UserId id) {

        User user = loadUser(id);

        user.suspend();

        return UserApplicationMapper.toResponse(
                userRepository.save(user)
        );
    }

    private User loadUser(UserId id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        UserNotFoundException.withId(id.getValue()));
    }


    public void assignRole(UUID userId, UUID roleId) {
        // TODO: Role assignment will be implemented in IAM M24.
    }

}