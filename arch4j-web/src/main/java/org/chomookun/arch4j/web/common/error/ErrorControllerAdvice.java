package org.chomookun.arch4j.web.common.error;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.message.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ErrorControllerAdvice {

    private final MessageSource messageSource;

    private final ErrorResponseHandler errorResponseHandler;

    /**
     * No Such Element Exception Handler
     * @param request request
     * @param response response
     * @param exception exception
     */
    @ExceptionHandler(NoSuchElementException.class)
    public void handleNoSuchElementException(HttpServletRequest request, HttpServletResponse response, NoSuchElementException exception) {
        errorResponseHandler.sendErrorResponse(request, response, HttpStatus.NOT_FOUND, exception);
    }

    /**
     * Method Argument Not Valid Exception Handler
     * @param request request
     * @param response response
     * @param exception exception
     * @return error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException exception) {
        List<String> errorMessages = new ArrayList<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errorMessages.add(errorMessage);
        });
        String errorMessage = String.join(", ", errorMessages);
        ErrorResponse errorResponse = ErrorResponse.from(request, HttpStatus.BAD_REQUEST, errorMessage);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Data Integrity Violation Exception Handler
     * @param request request
     * @param response response
     * @param exception response
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void handleDataIntegrityViolationException(HttpServletRequest request, HttpServletResponse response, DataIntegrityViolationException exception) {
        errorResponseHandler.sendErrorResponse(request, response, HttpStatus.CONFLICT, exception);
    }

}
