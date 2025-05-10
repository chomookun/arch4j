package org.chomookun.arch4j.web.view.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.security.SecurityService;
import org.chomookun.arch4j.core.user.UserService;
import org.chomookun.arch4j.core.user.model.User;
import org.chomookun.arch4j.core.verification.VerificationService;
import org.chomookun.arch4j.core.verification.model.*;
import org.chomookun.arch4j.web.common.error.ErrorResponse;
import org.chomookun.arch4j.web.common.error.ErrorResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("login")
@RequiredArgsConstructor
public class LoginController {

    private final ErrorResponseFactory errorResponseFactory;

    private final SecurityService securityService;

    private final UserService userService;

    private final VerificationService verificationService;

    @GetMapping
    public ModelAndView login() {
        return new ModelAndView("login/login");
    }

    @GetMapping("mfa")
    public ModelAndView mfa(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("login/login-mfa");
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        userService.getUser(userId).orElseThrow();
        modelAndView.addObject("availableVerifiers", verificationService.getAvailableVerifiers());
        return modelAndView;
    }

    @PostMapping("mfa/issue-code")
    @ResponseBody
    public Map<String,Object> issueCode(@RequestBody Map<String,Object> payload, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        User user = userService.getUser(userId).orElseThrow();
        String verifierId = Optional.ofNullable(payload.get("verifierId")).map(Object::toString).orElseThrow(IllegalArgumentException::new);

        // issue verification code
        String principal = switch (verifierId) {
            case "totp" -> user.getUserId();
            case "email" -> user.getEmail();
            case "mobile" -> user.getMobile();
            default -> throw new IllegalArgumentException("Invalid channel: " + verifierId);
        };
        IssueChallengeParam issueChallengeParam = IssueChallengeParam.builder()
                .verifierId(verifierId)
                .principal(principal)
                .reason("login")
                .build();
        IssueChallengeResult issueChallengeResult = verificationService.issueChallenge(issueChallengeParam);
        String verificationId = issueChallengeResult.getVerificationId();

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
        VerifyChallengeParam verifyChallengeParam = VerifyChallengeParam.builder()
                .verificationId(verificationId)
                .code(code)
                .build();
        VerifyChallengeResult verifyChallengeResult = verificationService.verifyChallenge(verifyChallengeParam);

        // success
        if (verifyChallengeResult.getResult() == Verification.Result.SUCCESS) {
            securityService.grantAuthentication(request, userId);
            return ResponseEntity.ok("Verification successful");
        }
        // failed
        else {
            String messageId = String.format("core.verification.Verification.Result.%s", verifyChallengeResult.getResult().name());
            RuntimeException exception = new RuntimeException(messageId);
            ErrorResponse errorResponse = errorResponseFactory.createErrorResponse(request, HttpStatus.UNAUTHORIZED, exception);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(errorResponse);
        }
    }

}
