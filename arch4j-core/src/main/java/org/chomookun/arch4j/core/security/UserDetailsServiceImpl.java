package org.chomookun.arch4j.core.security;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.security.model.Role;
import org.chomookun.arch4j.core.user.UserCredentialService;
import org.chomookun.arch4j.core.user.UserService;
import org.chomookun.arch4j.core.user.model.User;
import org.chomookun.arch4j.core.security.model.UserDetailsImpl;
import org.chomookun.arch4j.core.user.model.UserCredential;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SecurityProperties securityProperties;

    private final UserService userService;

    private final UserCredentialService userCredentialService;

    private final RoleService roleService;

    private final AuthorityService authorityService;

    /**
     * Loads user by username
     * @param username username
     * @return user details
     */
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // retrieves by username
        User user = userService.getUserByUsername(username).orElseThrow(()-> new UsernameNotFoundException(username));
        UserCredential passwordCredential = userCredentialService.getCredential(user.getUserId(), UserCredential.Type.PASSWORD).orElseThrow();
        // user details
        UserDetailsImpl userDetails =  UserDetailsImpl.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .username(user.getUsername())
                .password(passwordCredential.getCredential())
                .admin(user.isAdmin())
                .build();
        // checks disabled
        if(user.getStatus() == User.Status.CLOSED) {
            userDetails.setEnabled(false);
        }
        // checks account locked
        if(user.getStatus() == User.Status.LOCKED) {
            userDetails.setAccountNonLocked(false);
        }
        // checks account expired
        if(user.getExpireAt() != null) {
            if(user.getExpireAt().isAfter(Instant.now())) {
                userDetails.setAccountNonExpired(false);
            }
        }
        // checks credential expired
        if (securityProperties.getPasswordExpireDays() != null) {
            Instant passwordExpireAt = user.getPasswordAt().plus(securityProperties.getPasswordExpireDays(), ChronoUnit.DAYS);
            if(passwordExpireAt.isAfter(Instant.now())) {
                userDetails.setCredentialNonExpired(false);
            }
        }
        // add user roles
        userDetails.addRoles(user.getRoles());
        // roles
        final List<Role> roles = roleService.getRoles();
        // add anonymous roles
        userDetails.addRoles(roles.stream()
                .filter(Role::isAnonymous)
                .collect(Collectors.toList()));
        // add authenticated roles
        userDetails.addRoles(roles.stream()
                .filter(Role::isAuthenticated)
                .collect(Collectors.toList()));
        // in case of admin, add all roles and authorities
        if(user.isAdmin()) {
            userDetails.addRoles(roles);
            userDetails.addAuthorities(authorityService.getAuthorities());
        }
        // returns
        return userDetails;
    }

}
