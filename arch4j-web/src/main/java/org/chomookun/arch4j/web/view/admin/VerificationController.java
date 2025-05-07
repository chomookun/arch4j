package org.chomookun.arch4j.web.view.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.notification.NotificationService;
import org.chomookun.arch4j.core.notification.model.Notification;
import org.chomookun.arch4j.core.notification.model.NotificationSearch;
import org.chomookun.arch4j.core.verification.VerificationService;
import org.chomookun.arch4j.core.verification.VerifierService;
import org.chomookun.arch4j.core.verification.processor.VerifierProcessorDefinition;
import org.chomookun.arch4j.core.verification.processor.VerifierProcessorDefinitionRegistry;
import org.chomookun.arch4j.core.verification.model.*;
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

    private final VerifierService verifierService;

    private final NotificationService notificationService;

    private final ObjectMapper objectMapper;

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("admin/verification");
        List<VerifierProcessorDefinition> verifierProcessorDefinitions = VerifierProcessorDefinitionRegistry.getDefinitions();
        modelAndView.addObject("verifierProcessorDefinitions", verifierProcessorDefinitions);
        List<Notification> notifications = notificationService.getNotifications(NotificationSearch.builder().build(), Pageable.unpaged()).getContent();
        modelAndView.addObject("notifications", notifications);
        return modelAndView;
    }

    @GetMapping("get-verifications")
    @ResponseBody
    public Page<Verification> getVerifications(VerificationSearch verificationSearch, Pageable pageable) {
        return verificationService.getVerifications(verificationSearch, pageable);
    }

    @GetMapping("get-verifiers")
    @ResponseBody
    public Page<Verifier> getVerifiers(VerifierSearch verifierSearch, Pageable pageable) {
        return verifierService.getVerifiers(verifierSearch, pageable);
    }

    @PostMapping("save-verifier")
    @ResponseBody
    public Verifier saveVerifier(@RequestBody Verifier verifier) {
        return verifierService.saveVerifier(verifier);
    }

    @GetMapping("get-verifier")
    @ResponseBody
    public Verifier getVerifier(@RequestParam("verifierId") String verifierId) {
        return verifierService.getVerifier(verifierId).orElseThrow();
    }

    @DeleteMapping("delete-verifier")
    @ResponseBody
    public void deleteVerifier(@RequestParam("verifierId") String verifierId) {
        verifierService.deleteVerifier(verifierId);
    }

    @PostMapping("issue-challenge")
    @ResponseBody
    public IssueChallengeResult issueChallenge(@RequestBody IssueChallengeParam param) {
        return verificationService.issueChallenge(param);
    }

    @PostMapping("verify-challenge")
    @ResponseBody
    public VerifyChallengeResult verifyCode(@RequestBody VerifyChallengeParam param) {
        return verificationService.verifyChallenge(param);
    }

}
