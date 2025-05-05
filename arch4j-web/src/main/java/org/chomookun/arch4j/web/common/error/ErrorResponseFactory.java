package org.chomookun.arch4j.web.common.error;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.Nullable;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ErrorResponseFactory {

    private final MessageSource messageSource;

    @Nullable
    private final LocaleResolver localeResolver;

    /**
     * Creates error response
     * @param request request
     * @param status status
     * @param exception exception
     * @return error response
     */
    public ErrorResponse createErrorResponse(HttpServletRequest request, HttpStatus status, Exception exception) {
        String messageId = String.format("web.error.%s", exception.getClass().getSimpleName());
        Locale locale = localeResolver.resolveLocale(request);
        String message = messageSource.getMessage(messageId, null, locale);
        return ErrorResponse.builder()
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();
    }

    /**
     * Creates error response with redirect
     * @param request request
     * @param status status
     * @param exception exception
     * @param redirectUri redirect url
     * @return error response with redirect
     */
    public ErrorResponse createRedirectErrorResponse(HttpServletRequest request, HttpStatus status, Exception exception, String redirectUri) {
        ErrorResponse errorResponse = createErrorResponse(request, status, exception);
        errorResponse.setRedirect(true);
        errorResponse.setRedirectUri(redirectUri);
        return errorResponse;
    }

}
