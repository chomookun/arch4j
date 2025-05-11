package org.chomookun.arch4j.core.security;

import lombok.Builder;
import org.chomookun.arch4j.core.security.model.SecurityToken;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Builder
public class SecurityFilter extends OncePerRequestFilter {

    private final SecurityService securityService;

    private final SecurityTokenService securityTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // check security token
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String authorization = null;
        if(authorizationHeader != null && !authorizationHeader.trim().isEmpty()) {
            String[] authorizationHeaderArray = authorizationHeader.split(" ");
            if(authorizationHeaderArray.length >= 2) {
                authorization = authorizationHeaderArray[1];
            }
            // if security authorization exist
            if(authorization != null) {
                SecurityToken securityToken = securityTokenService.decodeSecurityToken(authorization);
                securityService.grantAuthentication(request, securityToken.getUserId());
            }
        }

        // populates granted authorities
        securityService.applyGrantedAuthorities();

        // forward
        filterChain.doFilter(request, response);
    }

}
