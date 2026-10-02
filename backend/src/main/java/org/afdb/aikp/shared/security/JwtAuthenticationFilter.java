package org.afdb.aikp.shared.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public JwtAuthenticationFilter(
            JwtService jwtService,
            AikpUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }


    private final JwtService jwtService;
    private final AikpUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String auth = request.getHeader("Authorization");

        if (auth == null || !auth.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = auth.substring(7);

        System.out.println("=== JWT FILTER DEBUG ===");
        System.out.println("AUTH HEADER PRESENT = true");
        System.out.println("TOKEN LENGTH = " + token.length());

        String email = jwtService.extractUsername(token);

        System.out.println("EXTRACTED EMAIL = " + email);

        if (email != null &&
            SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails user =
                userDetailsService.loadUserByUsername(email);

            System.out.println("JWT VALID = " + jwtService.isValid(token));

            if (jwtService.isValid(token)) {

                var authentication =
                    new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities());

                authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                        .buildDetails(request));

                SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
