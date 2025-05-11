package org.chomookun.arch4j.core.security;

import org.chomookun.arch4j.core.security.model.Role;
import org.chomookun.arch4j.core.security.model.UserDetailsImpl;
import org.chomookun.arch4j.core.user.CachedUserService;
import org.chomookun.arch4j.core.user.model.User;
import org.chomookun.arch4j.core.security.model.SecurityPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Lazy(false)
public class SecurityUtils {

    private static SecurityProperties securityProperties;

    private static CachedUserService cachedUserService;

    @Autowired
    public void setSecurityProperty(SecurityProperties securityPropertiesBean) {
        securityProperties = securityPropertiesBean;
    }

    @Autowired
    public void setCachedUserService(CachedUserService cachedUserServiceBean){
        cachedUserService = cachedUserServiceBean;
    }

    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(securityContext != null) {
            Authentication authentication = securityContext.getAuthentication();
            return authentication instanceof UsernamePasswordAuthenticationToken;
        }
        return false;
    }

    public static void checkAuthenticated() {
        if(!isAuthenticated()) {
            throw new InsufficientAuthenticationException("required authentication");
        }
    }

    public static Optional<UserDetailsImpl> getUserDetails() {
        UserDetailsImpl userDetails = null;
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(securityContext != null) {
            Authentication authentication = securityContext.getAuthentication();
            if(authentication instanceof UsernamePasswordAuthenticationToken) {
                userDetails = (UserDetailsImpl) authentication.getPrincipal();
            }
        }
        return Optional.ofNullable(userDetails);
    }

    public static Optional<User> getCurrentUser() {
        User user = getUserDetails()
                .flatMap(userDetails -> cachedUserService.getUser(userDetails.getUserId()))
                .orElse(null);
        return Optional.ofNullable(user);
    }

    public static String getCurrentUserId() {
        return getUserDetails()
                .map(UserDetailsImpl::getUserId)
                .orElse(null);
    }

    public static boolean isAdmin() {
        UserDetailsImpl userDetails = getUserDetails().orElse(null);
        return userDetails != null && userDetails.isAdmin();
    }

    public static boolean hasAnyRole(List<? extends Role> roles) {
        UserDetailsImpl userDetails = getUserDetails().orElse(null);
        if(userDetails == null) {
            return false;
        }
        return roles.stream().anyMatch(userDetails::hasRole);
    }

    public static boolean hasPermission(List<? extends Role> requiredRoles) {
        if(isAdmin()) {
            return true;
        }
        if(!requiredRoles.isEmpty()) {
            return hasAnyRole(requiredRoles);
        } else {
            return securityProperties.getSecurityPolicy() == SecurityPolicy.ANONYMOUS;
        }
    }

    public static void checkPermission(List<? extends Role> requiredRoles) {
        if(!hasPermission(requiredRoles)) {
            throw new InsufficientAuthenticationException("has no permission");
        }
    }

}
