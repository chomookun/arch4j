package org.chomookun.arch4j.web.view.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.notification.NotificationService;
import org.chomookun.arch4j.core.notification.model.Notification;
import org.chomookun.arch4j.core.notification.model.NotificationSearch;
import org.chomookun.arch4j.core.verification.VerificationService;
import org.chomookun.arch4j.core.verification.model.*;
import org.chomookun.arch4j.core.verification.verifier.IssueCodeRequest;
import org.chomookun.arch4j.core.verification.model.ChallengeResult;
import org.chomookun.arch4j.core.verification.verifier.VerifyCodeRequest;
import org.chomookun.arch4j.core.verification.model.VerifyResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@ConditionalOnProperty(prefix = "web.admin", name = "enabled", havingValue = "true", matchIfMissing = false)
@RequestMapping("admin/verification")
@PreAuthorize("hasAuthority('admin.verification')")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;

    private final NotificationService notificationService;

    private final ObjectMapper objectMapper;

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("admin/verification");
        List<String> types = verificationService.getAvailableTypes();
        modelAndView.addObject("types", types);
        List<Notification> notifications = notificationService.getNotifications(NotificationSearch.builder().build(), Pageable.unpaged()).getContent();
        modelAndView.addObject("notifications", notifications);
        return modelAndView;
    }

    @GetMapping("get-verifications")
    @ResponseBody
    public Page<Verification> getVerifications(VerificationSearch verificationSearch, Pageable pageable) {
        return verificationService.getVerifications(verificationSearch, pageable);
    }

    @PostMapping("issue-code")
    @ResponseBody
    public ChallengeResult requestVerification(@RequestBody IssueCodeRequest issueCodeRequest) throws JsonProcessingException {
        return verificationService.issueCode(issueCodeRequest);
    }

    @PatchMapping("verify-code")
    @ResponseBody
    public VerifyResult verifyCode(@RequestBody VerifyCodeRequest verifyCodeRequest) throws JsonProcessingException {
        return verificationService.verifyCode(verifyCodeRequest);
    }

}
