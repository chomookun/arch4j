package org.chomookun.arch4j.core.security;

import org.chomookun.arch4j.core.security.model.Authority;
import org.chomookun.arch4j.core.security.model.Role;
import org.chomookun.arch4j.core.security.model.UserDetailsImpl;
import org.chomookun.arch4j.core.security.model.SecurityPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
@Lazy(false)
public class SecurityUtils {

    private static SecurityProperties securityProperties;

    private static SecurityService securityService;

    @Autowired
    public void setSecurityProperty(SecurityProperties securityPropertiesBean) {
        securityProperties = securityPropertiesBean;
    }

    @Autowired
    public void setSecurityService(SecurityService securityServiceBean) {
        securityService = securityServiceBean;
    }

    public static Optional<String> getCurrentUserId() {
        String userId = null;
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(securityContext != null) {
            Authentication authentication = securityContext.getAuthentication();
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetailsImpl userDetails) {
                userId = userDetails.getUserId();
            }
        }
        return Optional.ofNullable(userId);
    }

    public static Optional<UserDetailsImpl> getCurrentUserDetails() {
        String userId = getCurrentUserId().orElse(null);
        if (userId != null) {
            return securityService.getUserDetails(userId);
        } else {
            return Optional.empty();
        }
    }

    public static boolean hasPermission(Collection<? extends Role> requiredRoles) {
        // returns true security policy is ANONYMOUS
        if (securityProperties.getSecurityPolicy() == SecurityPolicy.ANONYMOUS) {
            if (requiredRoles.isEmpty()) {
                return true;
            }
        }

        // checks user details
        UserDetailsImpl userDetails = getCurrentUserDetails().orElse(null);
        if (userDetails == null) {
            return false;
        }

        // checks admin
        if (userDetails.isAdmin()) {
            return true;
        }

        // checks has any required authority
        List<String> requiredAuthorities = requiredRoles.stream()
                .flatMap(role -> role.getAuthorities().stream())
                .map(Authority::getAuthorityId)
                .toList();
        List<String> userAuthorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        boolean hasAnyRequiredAuthority = requiredAuthorities.stream()
                .anyMatch(userAuthorities::contains);
        if (hasAnyRequiredAuthority) {
            return true;
        }

        // returns final negative result
        return false;
    }

}
