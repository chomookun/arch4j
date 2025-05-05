package org.chomookun.arch4j.web.common.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class ErrorResponseHandler {

    private final ErrorResponseFactory errorResponseFactory;

    private final ObjectMapper objectMapper;

    /**
     * Sends error response
     * @param request request
     * @param response response
     * @param errorResponse error response
     */
    public void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, ErrorResponse errorResponse) {
        try {
            if (isRestRequest(request)) {
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.setStatus(errorResponse.getStatus());
                String jsonResponse = objectMapper.writeValueAsString(errorResponse);
                response.getWriter().write(jsonResponse);
            } else {
                if (errorResponse.isRedirect()) {
                    response.sendRedirect(errorResponse.getRedirectUri());
                } else {
                    response.sendError(errorResponse.getStatus(), errorResponse.getMessage());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks is rest request
     * @param request http servlet request
     * @return whether if rest request or not
     */
    public boolean isRestRequest(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        String contentType = request.getHeader("Content-Type");
        String xRequestedWith = request.getHeader("X-Requested-With");
        log.debug("contentType: {}", contentType);
        log.debug("accept: {}", accept);
        if (accept != null && accept.contains("application/json")) {
            return true;
        }
        if (contentType != null && contentType.contains("application/json")){
            return true;
        }
        if (xRequestedWith != null && xRequestedWith.equals("XMLHttpRequest")){
            return true;
        }
        return false;
    }

    /**
     * Sends error response
     * @param request http servlet request
     * @param response http servlet response
     * @param status http status
     * @param exception exception
     */
    public void sendErrorResponse(HttpServletRequest request, HttpServletResponse response, HttpStatus status, Exception exception) {
        ErrorResponse errorResponse = errorResponseFactory.createErrorResponse(request, status, exception);
        sendErrorResponse(request, response, errorResponse);
    }

    /**
     * Sends error response with redirect
     * @param request http servlet request
     * @param response http servlet response
     * @param status http status
     * @param exception exception
     * @param redirectUri redirect url
     */
    public void sendRedirectErrorResponse(HttpServletRequest request, HttpServletResponse response, HttpStatus status, Exception exception, String redirectUri) {
        ErrorResponse errorResponse = errorResponseFactory.createRedirectErrorResponse(request, status, exception, redirectUri);
        sendErrorResponse(request, response, errorResponse);
    }

}
