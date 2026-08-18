package org.afdb.aikp.modules.iam.role.config;

import org.afdb.aikp.modules.iam.role.domain.repository.RoleRepository;
import org.afdb.aikp.modules.iam.role.domain.service.RoleDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleDomainConfiguration {

    @Bean
    public RoleDomainService roleDomainService(
            RoleRepository roleRepository) {

        return new RoleDomainService(roleRepository);
    }
}
