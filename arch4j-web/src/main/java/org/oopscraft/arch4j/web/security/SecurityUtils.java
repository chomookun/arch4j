package org.oopscraft.arch4j.web.security;

import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.security.UserDetailsImpl;
import org.oopscraft.arch4j.core.user.User;
import org.oopscraft.arch4j.core.user.UserService;
import org.oopscraft.arch4j.web.WebProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SecurityUtils {

    private static WebProperties webProperties;

    private static UserService userService;

    @Autowired
    public void setWebProperties(WebProperties webPropertiesBean) {
        webProperties = webPropertiesBean;
    }

    @Autowired
    public void setUserService(UserService userServiceBean){
        userService = userServiceBean;
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
                .flatMap(userDetails -> userService.getUser(userDetails.getUsername()))
                .orElse(null);
        return Optional.ofNullable(user);
    }

    public static String getCurrentUserId() {
        return getUserDetails()
                .map(UserDetailsImpl::getUsername)
                .orElse(null);
    }

    public static boolean hasAnyRole(List<Role> roles) {
        UserDetailsImpl userDetails = getUserDetails().orElse(null);
        if(userDetails == null) {
            return false;
        }
        return roles.stream().anyMatch(userDetails::hasRole);
    }

    public static boolean hasPermission(List<Role> requiredRoles) {
        if(!requiredRoles.isEmpty()) {
            return hasAnyRole(requiredRoles);
        }else{
            return webProperties.getSecurityPolicy() == SecurityPolicy.ANONYMOUS;
        }
    }

    public static void checkPermission(List<Role> requiredRoles) {
        if(!hasPermission(requiredRoles)) {
            throw new InsufficientAuthenticationException("has no permission");
        }
    }

}
