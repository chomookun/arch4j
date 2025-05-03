package org.chomookun.arch4j.web.view.admin;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.notification.NotificationService;
import org.chomookun.arch4j.core.notification.model.Notification;
import org.chomookun.arch4j.core.notification.model.NotificationSearch;
import org.chomookun.arch4j.core.security.support.SecurityUtils;
import org.chomookun.arch4j.core.verification.VerificationService;
import org.chomookun.arch4j.core.verification.model.Verification;
import org.chomookun.arch4j.core.verification.model.VerificationSearch;
import org.chomookun.arch4j.core.verification.model.VerifyResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@ConditionalOnProperty(prefix = "web.admin", name = "enabled", havingValue = "true", matchIfMissing = false)
@RequestMapping("admin/verification")
@PreAuthorize("hasAuthority('admin.verification')")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;

    private final NotificationService notificationService;

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("admin/verification");
        List<Notification> notifications = notificationService.getNotifications(NotificationSearch.builder().build(), Pageable.unpaged()).getContent();
        modelAndView.addObject("notifications", notifications);
        return modelAndView;
    }

    @GetMapping("get-verifications")
    @ResponseBody
    public Page<Verification> getVerifications(VerificationSearch verificationSearch, Pageable pageable) {
        return verificationService.getVerifications(verificationSearch, pageable);
    }

    @PostMapping("request-verification")
    @ResponseBody
    public Verification requestVerification(@RequestBody JsonNode jsonNode) {
        String notificationId = jsonNode.get("notificationId").asText();
        String principal = jsonNode.get("principal").asText();
        String reason = jsonNode.get("reason").asText();
        String userId = SecurityUtils.getCurrentUserId();
        return verificationService.requestVerification(notificationId, principal, reason, userId);
    }

    @PatchMapping("verify-code")
    @ResponseBody
    public Map<String,VerifyResult> verifyCode(@RequestBody JsonNode jsonNode) {
        String verificationId = jsonNode.get("verificationId").asText();
        String code = jsonNode.get("code").asText();
        VerifyResult verifyResult = verificationService.verifyCode(verificationId, code);
        return Map.of("verifyResult", verifyResult);
    }

}
