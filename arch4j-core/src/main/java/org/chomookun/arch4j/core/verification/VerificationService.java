package org.chomookun.arch4j.core.verification;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.notification.NotificationMessageService;
import org.chomookun.arch4j.core.notification.NotificationService;
import org.chomookun.arch4j.core.notification.model.Notification;
import org.chomookun.arch4j.core.notification.model.NotificationMessage;
import org.chomookun.arch4j.core.verification.entity.VerificationEntity;
import org.chomookun.arch4j.core.verification.model.Verification;
import org.chomookun.arch4j.core.verification.model.VerificationSearch;
import org.chomookun.arch4j.core.verification.model.VerifyResult;
import org.chomookun.arch4j.core.verification.repository.VerificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final NotificationService notificationService;

    private final NotificationMessageService notificationMessageService;

    private final VerificationRepository verificationRepository;

    /**
     * Requests verification
     * @param notificationId notification id
     * @param principal principal
     * @param reason purpose
     * @param userId user id
     * @return verification
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Verification requestVerification(String notificationId, String principal, String reason, String userId) {
        // notifies
        Notification notification = notificationService.getNotification(notificationId).orElseThrow();
        String subject = "Verification";
        String code = generateCode();
        String content = String.format("Verification code: %s", code);
        NotificationMessage notificationMessage = notificationMessageService.sendNotificationMessage(notification, principal, subject, content, null);
        // saves
        VerificationEntity verificationEntity = VerificationEntity.builder()
                .verificationId(IdGenerator.uuid())
                .notificationId(notificationId)
                .principal(principal)
                .reason(reason)
                .userId(userId)
                .notificationMessageId(notificationMessage.getMessageId())
                .code(code)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60 * 10))
                .tryCount(0)
                .verified(false)
                .verifiedAt(null)
                .build();
        VerificationEntity savedVerificationEntity = verificationRepository.save(verificationEntity);
        return Verification.from(savedVerificationEntity);
    }

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1_000_000));
    }

    /**
     * Verifies code
     * @param verificationId verification id
     * @param code code
     * @return verify result
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public VerifyResult verifyCode(String verificationId, String code) {
        VerificationEntity verificationEntity = verificationRepository.findById(verificationId).orElse(null);
        // not requested
        if (verificationEntity == null) {
            return VerifyResult.NOT_REQUESTED;
        }
        try {
            // already verified
            if (verificationEntity.isVerified()) {
                return VerifyResult.ALREADY_VERIFIED;
            }
            // expired
            if (verificationEntity.getExpiresAt().isBefore(Instant.now())) {
                return VerifyResult.EXPIRED;
            }
            // too many tries
            if (verificationEntity.getTryCount() > 10) {
                return VerifyResult.TOO_MANY_TRIES;
            }
            // check code
            if (Objects.equals(code, verificationEntity.getCode())) {
                verificationEntity.setVerified(true);
                verificationEntity.setVerifiedAt(Instant.now());
                return VerifyResult.SUCCESS;
            } else {
                return VerifyResult.INVALID_CODE;
            }
        } finally {
            verificationEntity.setTryCount(verificationEntity.getTryCount() + 1);
            verificationRepository.save(verificationEntity);
        }
    }

    /**
     * Gets verification
     * @param verificationSearch verification search
     * @param pageable pageable
     * @return page of verification
     */
    public Page<Verification> getVerifications(VerificationSearch verificationSearch, Pageable pageable) {
        Page<VerificationEntity> verificationEntityPage = verificationRepository.findAll(verificationSearch, pageable);
        List<Verification> verifications = verificationEntityPage.getContent().stream()
                .map(Verification::from)
                .toList();
        long total = verificationEntityPage.getTotalElements();
        return new PageImpl<>(verifications, pageable, total);
    }

}
