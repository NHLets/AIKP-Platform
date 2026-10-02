package org.afdb.aikp.shared.security;

import org.afdb.aikp.modules.iam.domain.model.User;
import org.afdb.aikp.modules.iam.domain.repository.UserRepository;
import org.afdb.aikp.modules.iam.domain.valueobject.Email;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AikpUserDetailsService implements UserDetailsService {

    public AikpUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(Email.of(email))
                .orElseThrow(() ->
                        new UsernameNotFoundException(email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail().value(),
                user.getPasswordHash().value(),
                List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRole().getName().value()
                        )
                )
        );
    }
}
