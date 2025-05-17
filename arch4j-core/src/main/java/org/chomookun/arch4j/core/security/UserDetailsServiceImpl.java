package org.chomookun.arch4j.core.security;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.security.model.Role;
import org.chomookun.arch4j.core.user.CachedUserService;
import org.chomookun.arch4j.core.user.UserService;
import org.chomookun.arch4j.core.user.model.User;
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

    private final UserService userService;

    private final SecurityService securityService;

    /**
     * Loads user by username
     * @param username username
     * @return user details
     */
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return securityService.getUserDetails(user.getUserId()).orElseThrow();
    }

}
