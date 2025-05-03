package org.chomookun.arch4j.core.verification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.notification.NotificationMessageService;
import org.chomookun.arch4j.core.notification.NotificationService;
import org.chomookun.arch4j.core.notification.model.Notification;
import org.chomookun.arch4j.core.notification.model.NotificationMessage;
import org.chomookun.arch4j.core.verification.entity.VerificationEntity;
import org.chomookun.arch4j.core.verification.model.*;
import org.chomookun.arch4j.core.verification.repository.VerificationRepository;
import org.chomookun.arch4j.core.verification.verifier.Verifier;
import org.chomookun.arch4j.core.verification.verifier.VerifierFactory;
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
@Slf4j
public class VerificationService {

    private final NotificationService notificationService;

    private final NotificationMessageService notificationMessageService;

    private final VerificationRepository verificationRepository;

    private final VerifierFactory verifierFactory;

    public List<String> getAvailableTypes() {
        return verifierFactory.getAvailableTypes();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public IssueCodeResponse issueCode(IssueCodeRequest request) {
        // saves
        VerificationEntity verificationEntity = VerificationEntity.builder()
                .verificationId(IdGenerator.uuid())
                .type(request.getType())
                .notificationId(request.getNotificationId())
                .principal(request.getPrincipal())
                .reason(request.getReason())
                .userId(request.getUserId())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60 * 10))
                .tryCount(0)
                .verified(false)
                .verifiedAt(null)
                .build();
        verificationRepository.save(verificationEntity);
        Verifier verifier = verifierFactory.getVerifier(request.getType());
        IssueCodeResponse issueCodeResponse = verifier.issueCode(request);
        verificationEntity.setCode(issueCodeResponse.getCode());
        verificationEntity.setNotificationMessageId(issueCodeResponse.getNotificationMessageId());
        verificationRepository.save(verificationEntity);
        return issueCodeResponse;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public VerifyCodeResponse verifyCode(VerifyCodeRequest request) {
        VerificationEntity verificationEntity = verificationRepository.findById(request.getVerificationId()).orElse(null);
        verificationEntity.setTryCount(verificationEntity.getTryCount() + 1);
        Verifier verifier = verifierFactory.getVerifier(verificationEntity.getType());
        VerifyCodeResponse verifyCodeResponse = verifier.verifyCode(request);
        if (verifyCodeResponse.getVerifyResult() == VerifyResult.SUCCESS) {
            verificationEntity.setVerified(true);
            verificationEntity.setVerifiedAt(Instant.now());
            verificationRepository.save(verificationEntity);
        }
        return verifyCodeResponse;
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
