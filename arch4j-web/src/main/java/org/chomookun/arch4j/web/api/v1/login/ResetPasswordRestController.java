package org.chomookun.arch4j.web.api.v1.login;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.email.EmailService;
import org.chomookun.arch4j.web.common.error.ErrorResponse;
import org.chomookun.arch4j.core.user.model.User;
import org.chomookun.arch4j.core.user.UserService;
import org.chomookun.arch4j.web.api.v1.login.dto.ResetPasswordRequest;
import org.chomookun.arch4j.web.common.error.ErrorResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Tag(name = "login")
@RestController
@RequestMapping("api/v1/login/reset-password")
@RequiredArgsConstructor
public class ResetPasswordRestController {

    private final UserService userService;

    private final EmailService emailService;

    private final ErrorResponseFactory errorResponseFactory;

    private final HttpServletRequest request;

    @PostMapping
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        // checks already existing user
        User user = userService.getUserByUsername(resetPasswordRequest.getUsername()).orElseThrow();
        // check answer code
        try {
            emailService.validateEmailVerification(resetPasswordRequest.getUsername(), resetPasswordRequest.getAnswer());
        } catch (Exception e) {
            ErrorResponse errorResponse = errorResponseFactory.createErrorResponse(request, HttpStatus.BAD_REQUEST, e);
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }

        // update password
        userService.changePassword(user.getUserId(), resetPasswordRequest.getPassword());

        // response
        return ResponseEntity.ok().build();
    }

    @GetMapping("validate-email/{email}")
    public ResponseEntity<?> validateEmail(@PathVariable("email")String email) {
        // check duplicated email
        User user = userService.getUserByUsername(email).orElseThrow();

        // issue verification
        emailService.issueEmailVerification(email);

        // response
        return ResponseEntity.ok().build();
    }

    @GetMapping("validate-email/{email}/answer/{answer}")
    public ResponseEntity<?> validateEmailAnswer(@PathVariable("email")String email, @PathVariable("answer")String answer) {
        try {
            emailService.validateEmailVerification(email, answer);
        } catch (Exception e) {
            ErrorResponse errorResponse = errorResponseFactory.createErrorResponse(request, HttpStatus.BAD_REQUEST, e);
            return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
        }
        return ResponseEntity.ok().build();
    }

}
