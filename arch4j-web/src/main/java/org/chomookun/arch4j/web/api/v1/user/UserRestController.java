package org.chomookun.arch4j.web.api.v1.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.web.common.error.ErrorResponse;
import org.chomookun.arch4j.core.user.model.User;
import org.chomookun.arch4j.core.user.UserService;
import org.chomookun.arch4j.core.security.support.SecurityUtils;
import org.chomookun.arch4j.web.api.v1.user.dto.ChangePasswordRequest;
import org.chomookun.arch4j.web.api.v1.user.dto.UserRequest;
import org.chomookun.arch4j.web.api.v1.user.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@Tag(name = "user", description = "User")
public class UserRestController {

    private final UserService userService;

    private final HttpServletRequest request;

    @GetMapping("{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId")String userId) {
        UserResponse userResponse = userService.getUser(userId)
                .map(UserResponse::from)
                .orElseThrow();
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> modifyUser(@PathVariable("userId")String userId, @RequestBody UserRequest userRequest) {
        String currentUserId = SecurityUtils.getCurrentUserId();
        if(!userId.equals(currentUserId)) {
            throw new AccessDeniedException("Not login user");
        }
        User user = userService.getUser(currentUserId).orElseThrow();
        user.setUsername(userRequest.getUsername());
        user.setMobile(userRequest.getMobile());
        user.setPhoto(userRequest.getPhoto());
        user.setProfile(userRequest.getProfile());
        user = userService.saveUser(user);
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @PutMapping("{userId}/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> changeUserPassword(
            @PathVariable("userId")String id,
            @RequestBody ChangePasswordRequest changePasswordRequest
    ) {
        String currentUserId = SecurityUtils.getCurrentUserId();
        if(!id.equals(currentUserId)) {
            throw new AccessDeniedException("Not login user");
        }
        if(userService.isPasswordMatched(currentUserId, changePasswordRequest.getCurrentPassword())){
            userService.changePassword(currentUserId, changePasswordRequest.getNewPassword());
        }else{
            ErrorResponse errorResponse = ErrorResponse.from(request, HttpStatus.BAD_REQUEST, "password not matched");
            return ResponseEntity
                    .status(errorResponse.getStatus())
                    .body(errorResponse);
        }
        return ResponseEntity.ok().build();
    }

}
