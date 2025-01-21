package org.oopscraft.arch4j.web.join.api.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.email.service.EmailService;
import org.oopscraft.arch4j.web.common.error.ErrorResponse;
import org.oopscraft.arch4j.core.security.model.User;
import org.oopscraft.arch4j.core.security.service.UserService;
import org.oopscraft.arch4j.web.join.api.v1.dto.JoinRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/join")
@RequiredArgsConstructor
@Tag(name = "join", description = "Join")
public class JoinRestController {

    private final UserService userService;

    private final EmailService emailService;

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
            ErrorResponse errorResponse = ErrorResponse.from(request, HttpStatus.CONFLICT, "duplicated user id");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
        return ResponseEntity.ok().build();
    }

//    @Deprecated
//    @GetMapping("validate-email/{email}")
//    public ResponseEntity<?> validateEmail(@PathVariable("email")String email) {
//
//        // check duplicated email
//        User user = userService.getUserByEmail(email).orElse(null);
//        if(user != null) {
//            ErrorResponse errorResponse = ErrorResponse.builder()
//                    .status(HttpStatus.CONFLICT)
//                    .message("duplicated email")
//                    .build();
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
//        }
//
//        // issue verification
//        emailService.issueEmailVerification(email);
//
//        // response
//        return ResponseEntity.ok().build();
//    }

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
