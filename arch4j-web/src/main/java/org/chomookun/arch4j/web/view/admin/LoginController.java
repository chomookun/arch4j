package org.chomookun.arch4j.web.view.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.security.SecurityService;
import org.chomookun.arch4j.core.user.UserService;
import org.chomookun.arch4j.core.user.model.User;
import org.chomookun.arch4j.core.verification.VerificationService;
import org.chomookun.arch4j.core.verification.model.ChallengeResult;
import org.chomookun.arch4j.core.verification.model.VerifyResult;
import org.chomookun.arch4j.web.common.error.ErrorResponse;
import org.chomookun.arch4j.web.common.error.ErrorResponseFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@ConditionalOnProperty(prefix = "web.admin", name = "enabled", havingValue = "true", matchIfMissing = false)
@RequestMapping("/admin/login")
@RequiredArgsConstructor
public class LoginController {

    private final VerificationService verificationService;

    private final ErrorResponseFactory errorResponseFactory;

    private final SecurityService securityService;

    private final UserDetailsService userDetailsService;

    private final UserService userService;

    @GetMapping
    public ModelAndView login() {
        return new ModelAndView("admin/login");
    }

    @GetMapping("mfa")
    public ModelAndView mfa(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("admin/login-mfa");
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        User user = userService.getUser(userId).orElseThrow();
        List<String> type = new ArrayList<>();
        type.add("totp");    // default TOTP
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            type.add("email");
        }
        if (user.getMobile() != null && !user.getMobile().isEmpty()) {
            type.add("mobile");
        }
        modelAndView.addObject("options", type);
        return modelAndView;
    }

    @PostMapping("mfa/issue-code")
    @ResponseBody
    public Map<String,Object> issueCode(@RequestBody Map<String,Object> payload, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        String verificationId = Optional.ofNullable(payload.get("verificationId")).map(Object::toString).orElseThrow(IllegalArgumentException::new);
        User user = userService.getUser(userId).orElseThrow();

        // issue verification code
        String type = null;
        String notificationId = null;
        String principal = null;
        switch (channel){
            case "totp" -> {
                type = "totp";
                principal = userId;
            }
            case "email" -> {
                type = "notification";
                notificationId = "email";
                principal = user.getEmail();
            }
            case "mobile" -> {
                type = "notification";
                notificationId = "mobile";
                principal = user.getMobile();
            }
            default -> throw new IllegalArgumentException("Invalid channel: " + channel);
        }
        IssueCodeRequest issueCodeRequest = IssueCodeRequest.builder()
                .verificationId(verificationId)
                .principal(principal)
                .build();
        ChallengeResult issueCodeResponse = verificationService.issueCode(issueCodeRequest);
        String verificationId = issueCodeResponse.getVerificationId();

        // response
        return Map.of("verificationId", verificationId);
    }

    @PostMapping("mfa/verify-code")
    @ResponseBody
    public ResponseEntity<?> verifyCode(@RequestBody Map<String,Object> payload, HttpServletRequest request) {
        String userId = (String)request.getSession().getAttribute("userId");
        String verificationId = Optional.ofNullable(payload.get("verificationId")).map(Object::toString).orElseThrow(IllegalArgumentException::new);
        String code = Optional.ofNullable(payload.get("code")).map(Object::toString).orElseThrow(IllegalArgumentException::new);

        // issue verification code
        VerifyCodeRequest verifyCodeRequest = VerifyCodeRequest.builder()
                .verificationId(verificationId)
                .code(code)
                .build();
        VerifyResult verifyCodeResponse = verificationService.verifyCode(verifyCodeRequest);

        // success
        if (verifyCodeResponse.getVerifyResult() == VerifyResult.SUCCESS) {
            securityService.grantAuthentication(request, userId);
            return ResponseEntity.ok("Verification successful");
        }
        // failed
        else {
            RuntimeException exception = new RuntimeException("Verification failed");
            ErrorResponse errorResponse = errorResponseFactory.createErrorResponse(request, HttpStatus.UNAUTHORIZED, exception);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(errorResponse);
        }
    }

}
