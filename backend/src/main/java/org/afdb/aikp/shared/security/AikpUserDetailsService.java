package org.afdb.aikp.shared.security;

import lombok.RequiredArgsConstructor;
import org.afdb.aikp.modules.iam.domain.model.User;
import org.afdb.aikp.modules.iam.domain.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AikpUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
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
