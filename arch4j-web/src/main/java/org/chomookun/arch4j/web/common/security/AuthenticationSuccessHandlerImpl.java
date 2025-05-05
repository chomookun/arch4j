package org.chomookun.arch4j.web.common.security;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.security.model.UserDetailsImpl;
import org.chomookun.arch4j.core.user.UserService;
import org.chomookun.arch4j.core.user.model.User;
import org.chomookun.arch4j.web.common.error.ErrorResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final UserService userService;

    private final ErrorResponseHandler errorResponseHandler;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userService.getUser(userDetails.getUserId()).orElseThrow();

        // checks MFA
        if (user.isMfaEnabled()) {
            // clears authentication
            SecurityContextHolder.clearContext();
            request.getSession().removeAttribute("SPRING_SECURITY_CONTEXT");

            // set session attribute
            HttpSession session = request.getSession();
            session.setAttribute("userId", userDetails.getUserId());

            // redirect to MFA page
            String requestUri = request.getRequestURI();
            String redirectUri;
            if(requestUri.startsWith("/admin")) {
                redirectUri = "/admin/login/mfa";
            }else{
                redirectUri = "/login/mfa";
            }
            RuntimeException exception = new RuntimeException("MFA required");
            errorResponseHandler.sendRedirectErrorResponse(request, response, HttpStatus.UNAUTHORIZED, exception, redirectUri);
        }
    }

}
