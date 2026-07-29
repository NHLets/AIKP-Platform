package org.afdb.aikp.modules.iam.application.service;

import org.afdb.aikp.modules.iam.application.command.CreateUserCommand;
import org.afdb.aikp.modules.iam.application.command.UpdateUserCommand;
import org.afdb.aikp.modules.iam.application.mapper.UserApplicationMapper;
import org.afdb.aikp.modules.iam.application.response.UserResponse;
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

import java.util.List;

@Service
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

    public UserResponse create(CreateUserCommand command) {

        PasswordHash passwordHash =
                PasswordHash.of(passwordEncoder.encode(command.password()));

        User user = userDomainService.createUser(
                Username.of(command.username()),
                Email.of(command.email()),
                FullName.of(command.fullName()),
                passwordHash);

        return UserApplicationMapper.toResponse(user);
    }

    public UserResponse findById(UserId id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.withId(id.getValue()));

        return UserApplicationMapper.toResponse(user);
    }

    public List<UserResponse> findAll() {
        return UserApplicationMapper.toResponseList(userRepository.findAll());
    }
}