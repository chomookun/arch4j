package org.chomookun.arch4j.web.api.v1.join;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.email.EmailService;
import org.chomookun.arch4j.web.common.error.ErrorResponse;
import org.chomookun.arch4j.core.user.model.User;
import org.chomookun.arch4j.core.user.UserService;
import org.chomookun.arch4j.web.api.v1.join.dto.JoinRequest;
import org.chomookun.arch4j.web.common.error.ErrorResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "join")
@RestController
@RequestMapping("api/v1/join")
@RequiredArgsConstructor
public class JoinRestController {

    private final UserService userService;

    private final EmailService emailService;

    private final ErrorResponseFactory errorResponseFactory;

    @PostMapping
    public ResponseEntity<?> join(@RequestBody @Validated JoinRequest joinRequest) {

        // checks already existing user
        User user = userService.getUser(joinRequest.getUserId()).orElse(null);
        if(user != null) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .message("duplicated user id")
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        // check answer code
        try {
            emailService.validateEmailVerification(joinRequest.getEmail(), joinRequest.getAnswer());
        } catch (Throwable t) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .message(t.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        // save user
        user = User.builder()
                .userId(joinRequest.getUserId())
                .username(joinRequest.getUserName())
                .password(joinRequest.getPassword())
                .build();
        userService.saveUser(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("validate-user-id/{userId}")
    public ResponseEntity<?> validateUserId(@PathVariable("userId")String userId, HttpServletRequest request) {
        User user = userService.getUser(userId).orElse(null);
        if(user != null) {
            ErrorResponse errorResponse = errorResponseFactory.createErrorResponse(request, HttpStatus.CONFLICT, new RuntimeException("duplicated user id"));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("validate-email/{email}/answer/{answer}")
    public ResponseEntity<?> validateEmailAnswer(@PathVariable("email")String email, @PathVariable("answer")String answer) {
        try {
            emailService.validateEmailVerification(email, answer);
        } catch (Throwable t) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .message(t.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        return ResponseEntity.ok().build();
    }

}
