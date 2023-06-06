package org.oopscraft.arch4j.web.security;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.security.AuthenticationTokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Builder
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final AuthenticationTokenService authenticationTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // check authentication token
        String authenticationToken = authenticationTokenService.parseAuthenticationToken(request);
        if(authenticationToken != null) {
            UserDetails userDetails = authenticationTokenService.decodeAuthenticationToken(authenticationToken);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            // keep alive
            authenticationToken = authenticationTokenService.encodeAuthenticationToken(userDetails);
            authenticationTokenService.issueAuthenticationToken(response, authenticationToken);
        }

        // forward
        filterChain.doFilter(request, response);
    }

}
