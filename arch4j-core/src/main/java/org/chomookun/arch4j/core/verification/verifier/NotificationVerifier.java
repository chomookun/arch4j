package org.chomookun.arch4j.core.verification.verifier;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.notification.NotificationMessageService;
import org.chomookun.arch4j.core.notification.NotificationService;
import org.chomookun.arch4j.core.notification.model.Notification;
import org.chomookun.arch4j.core.notification.model.NotificationMessage;
import org.chomookun.arch4j.core.verification.entity.VerificationEntity;
import org.chomookun.arch4j.core.verification.model.*;
import org.chomookun.arch4j.core.verification.repository.VerificationRepository;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class NotificationVerifier extends Verifier {

    private final NotificationService notificationService;

    private final NotificationMessageService notificationMessageService;

    private final VerificationRepository verificationRepository;

    @Override
    public String getType() {
        return "notification";
    }

    @Override
    public IssueCodeResponse issueCode(IssueCodeRequest request) {
        Notification notification = notificationService.getNotification(request.getNotificationId()).orElseThrow();
        String to = request.getPrincipal();
        String subject = "Verification";
        String code = generateCode();
        String content = String.format("Verification code: %s", code);
        NotificationMessage notificationMessage = notificationMessageService.sendNotificationMessage(notification, to, subject, content, null);
        return IssueCodeResponse.builder()
                .code(code)
                .notificationMessageId(notificationMessage.getMessageId())
                .build();
    }

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1_000_000));
    }

    @Override
    public VerifyCodeResponse verifyCode(VerifyCodeRequest request) {
        String verificationId = request.getVerificationId();
        VerificationEntity verificationEntity = verificationRepository.findById(verificationId).orElse(null);
        // not requested
        if (verificationEntity == null) {
            return VerifyCodeResponse.builder()
                    .verifyResult(VerifyResult.NOT_REQUESTED)
                    .build();
        }
        try {
            // already verified
            if (verificationEntity.isVerified()) {
                return VerifyCodeResponse.builder()
                        .verifyResult(VerifyResult.ALREADY_VERIFIED)
                        .build();
            }
            // expired
            if (verificationEntity.getExpiresAt().isBefore(Instant.now())) {
                return VerifyCodeResponse.builder()
                        .verifyResult(VerifyResult.EXPIRED)
                        .build();
            }
            // too many tries
            if (verificationEntity.getTryCount() > 10) {
                return VerifyCodeResponse.builder()
                        .verifyResult(VerifyResult.TOO_MANY_TRIES)
                        .build();
            }
            // check code
            if (Objects.equals(request.getCode(), verificationEntity.getCode())) {
                verificationEntity.setVerified(true);
                verificationEntity.setVerifiedAt(Instant.now());
                return VerifyCodeResponse.builder()
                        .verifyResult(VerifyResult.SUCCESS)
                        .build();
            } else {
                return VerifyCodeResponse.builder()
                        .verifyResult(VerifyResult.INVALID_CODE)
                        .build();
            }
        } finally {
            verificationEntity.setTryCount(verificationEntity.getTryCount() + 1);
            verificationRepository.save(verificationEntity);
        }
    }

}
