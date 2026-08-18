package org.afdb.aikp.modules.iam.config;

import org.afdb.aikp.modules.iam.domain.repository.UserRepository;
import org.afdb.aikp.modules.iam.domain.service.UserDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IamDomainConfiguration {

    @Bean
    public UserDomainService userDomainService(
            UserRepository userRepository) {

        return new UserDomainService(userRepository);
    }
}
