package org.oopscraft.arch4j.web.security;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.role.AuthorityService;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.role.RoleService;
import org.oopscraft.arch4j.core.user.User;
import org.oopscraft.arch4j.core.user.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    private final RoleService roleService;

    private final AuthorityService authorityService;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // retrieves by username
        User user = userService.getUser(username).orElseThrow(()->{
            throw new UsernameNotFoundException(username);
        });

        // user details
        UserDetailsImpl userDetails =  UserDetailsImpl.from(user);

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

        return userDetails;
    }

}
