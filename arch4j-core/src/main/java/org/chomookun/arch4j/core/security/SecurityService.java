package org.chomookun.arch4j.core.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.security.model.Authority;
import org.chomookun.arch4j.core.security.model.GrantedAuthorityImpl;
import org.chomookun.arch4j.core.security.model.Role;
import org.chomookun.arch4j.core.security.model.UserDetailsImpl;
import org.chomookun.arch4j.core.user.CachedUserService;
import org.chomookun.arch4j.core.user.model.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final SecurityProperties securityProperties;

    private final CachedUserService cachedUserService;

    private final CachedRoleService cachedRoleService;

    private final CachedAuthorityService cachedAuthorityService;

    /**
     * Gets user details
     * @param userId user id
     * @return user details impl
     */
    public UserDetailsImpl getUserDetails(String userId) {
        User user = cachedUserService.getUser(userId).orElseThrow();

        // user details
        UserDetailsImpl userDetails =  UserDetailsImpl.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .password(user.getPassword())
                .name(user.getName())
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

        // returns
        return userDetails;
    }

    public void grantAuthentication(HttpServletRequest request, String userId) {
        UserDetailsImpl userDetails = getUserDetails(userId);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }

    public void revokeAuthentication(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        request.getSession().removeAttribute("SPRING_SECURITY_CONTEXT");
    }

    public void applyGrantedAuthorities() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        // creates and updates anonymous security token with anonymous authorities
        if(authentication instanceof AnonymousAuthenticationToken anonymousAuthenticationToken) {
            Collection<GrantedAuthority> anonymousAuthorities = getAnonymousGrantedAuthorities();
            securityContext.setAuthentication(new AnonymousAuthenticationToken(
                    UUID.randomUUID().toString(),
                    anonymousAuthenticationToken.getPrincipal(),
                    anonymousAuthorities));
        }

        // creates and updates user details
        if(authentication instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
            UserDetailsImpl userDetails = (UserDetailsImpl) usernamePasswordAuthenticationToken.getPrincipal();
            Collection<GrantedAuthority> authenticatedAuthorities = getUserGrantedAuthorities(userDetails.getUserId());
            securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    authenticatedAuthorities
            ));
        }
        System.out.println("granted authorities" + securityContext.getAuthentication().getAuthorities());
    }

    private Collection<GrantedAuthority> getUserGrantedAuthorities(String userId) {
        final User user = cachedUserService.getUser(userId).orElseThrow();
        final List<Role> roles = cachedRoleService.getRoles();
        final List<Authority> authorities = cachedAuthorityService.getAuthorities();
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        final Set<Role> userRoles = new HashSet<>();

        // anonymous roles
        userRoles.addAll(roles.stream()
                .filter(Role::isAnonymous)
                .toList());
        // authenticated roles
        userRoles.addAll(roles.stream()
                .filter(Role::isAuthenticated)
                .toList());
        // add user roles
        userRoles.addAll(user.getRoles());

        // granted authorities
        for (Role role : userRoles) {
            grantedAuthorities.add(GrantedAuthorityImpl.from(role));
            role.getAuthorities().forEach(authority -> {
                grantedAuthorities.add(GrantedAuthorityImpl.from(authority));
            });
        }

        // in case of admin, add all roles and authorities
        if(user.isAdmin()) {
            grantedAuthorities.addAll(roles.stream()
                    .map(GrantedAuthorityImpl::from)
                    .toList());
            grantedAuthorities.addAll(authorities.stream()
                    .map(GrantedAuthorityImpl::from)
                    .toList());
        }

        // returns
        return grantedAuthorities;
    }

    /**
     * gets anonymous authorities
     * @return anonymous authorities
     */
    private Collection<GrantedAuthority> getAnonymousGrantedAuthorities() {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        List<Role> anonymousRoles = cachedRoleService.getRoles().stream()
                .filter(Role::isAnonymous)
                .toList();
        anonymousRoles.forEach(role -> {
            grantedAuthorities.add(GrantedAuthorityImpl.from(role));
            role.getAuthorities().forEach(authority -> {
                grantedAuthorities.add(GrantedAuthorityImpl.from(authority));
            });
        });
        return grantedAuthorities;
    }

}
