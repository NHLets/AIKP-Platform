package org.afdb.aikp.modules.iam.application.service;

import org.afdb.aikp.modules.iam.application.command.CreateUserCommand;
import org.afdb.aikp.modules.iam.application.command.UpdateUserCommand;
import org.afdb.aikp.modules.iam.application.mapper.UserApplicationMapper;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserApplicationService {

    private final UserRepository userRepository;
    private final UserDomainService userDomainService;
    private final PasswordEncoder passwordEncoder;

    public UserApplicationService(
            UserRepository userRepository,
            UserDomainService userDomainService,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.userDomainService = userDomainService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse create(CreateUserCommand command) {

        PasswordHash passwordHash =
                PasswordHash.of(passwordEncoder.encode(command.password()));

        User user = userDomainService.createUser(
                Username.of(command.username()),
                Email.of(command.email()),
                FullName.of(command.fullName()),
                passwordHash
        );

        User saved = userRepository.save(user);

        return UserApplicationMapper.toResponse(saved);
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
}