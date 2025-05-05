package org.chomookun.arch4j.core.verification;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.user.model.User;
import org.chomookun.arch4j.core.verification.entity.VerificationChallengeEntity;
import org.chomookun.arch4j.core.verification.entity.VerificationEntity;
import org.chomookun.arch4j.core.verification.model.*;
import org.chomookun.arch4j.core.verification.repository.VerificationIssueRepository;
import org.chomookun.arch4j.core.verification.repository.VerificationRepository;
import org.chomookun.arch4j.core.verification.verifier.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VerificationChallengeService {

    private final VerificationRepository verificationRepository;

    private final VerificationIssueRepository verificationIssueRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void challenge(String verificationId, User user, String reason) {
        VerificationEntity verificationEntity = verificationRepository.findById(verificationId).orElseThrow();
        Verification verification = Verification.from(verificationEntity);

        // saves
        VerificationChallengeEntity verificationIssueEntity = VerificationChallengeEntity.builder()
                .issueId(IdGenerator.uuid())
                .issuedAt(Instant.now())
                .verificationId(verification.getVerificationId())
                .reason(reason)
                .userId(user.getUserId())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60 * 10))
                .tryCount(0)
                .verified(false)
                .verifiedAt(null)
                .build();
        verificationIssueRepository.save(verificationIssueEntity);
        VerificationIssue verificationIssue = VerificationIssue.from(verificationIssueEntity);

        // calls verifier
        VerifierDefinition verifierDefinition = VerifierDefinitionRegistry.getVerifierDefinition(verificationEntity.getVerifierType()).orElseThrow();
        VerifierFactory verifierFactory = VerifierFactoryRegistry.getVerifierFactory(verifierDefinition).orElseThrow();
        Verifier verifier = verifierFactory.getVerifier(verification);
        verifier.issueCode(verificationIssue);

        // updates verification issue
        verificationIssueEntity.setCode(verificationIssue.getCode());
        verificationIssueEntity.setNotificationMessageId(verificationIssue.getNotificationMessageId());
        verificationIssueRepository.save(verificationIssueEntity);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public VerifyResult verifyCode(String issueId, String code) {
        VerificationChallengeEntity verificationIssueEntity = verificationIssueRepository.findById(issueId).orElseThrow();
        verificationIssueEntity.setTryCount(verificationIssueEntity.getTryCount() + 1);
        VerificationIssue verificationIssue = VerificationIssue.from(verificationIssueEntity);

        VerificationEntity verificationEntity = verificationRepository.findById(verificationIssueEntity.getVerificationId()).orElseThrow();
        Verification verification = Verification.from(verificationEntity);

        // calls verifier
        VerifierDefinition verifierDefinition = VerifierDefinitionRegistry.getVerifierDefinition(verificationEntity.getVerifierType()).orElseThrow();
        VerifierFactory verifierFactory = VerifierFactoryRegistry.getVerifierFactory(verifierDefinition).orElseThrow();
        Verifier verifier = verifierFactory.getVerifier(verification);
        boolean result = verifier.verifyCode(verificationIssue, code);

        // updates verification issue
        if (result) {
            verificationIssueEntity.setVerified(true);
            verificationIssueEntity.setVerifiedAt(Instant.now());
        }
        verificationIssueRepository.save(verificationIssueEntity);
        return VerifyResult.builder()
                .result(VerifyResult.Result.SUCCESS)
                .build();
    }

    public Page<VerificationIssue> getVerificationIssues(VerificationIssueSearch verificationIssueSearch, Pageable pageable) {
        Page<VerificationChallengeEntity> verificationIssueEntityPage = verificationIssueRepository.findAll(verificationIssueSearch, pageable);
        List<VerificationIssue> verificationIssues = verificationIssueEntityPage.getContent().stream()
                .map(VerificationIssue::from)
                .toList();
        long total = verificationIssueEntityPage.getTotalElements();
        return new PageImpl<>(verificationIssues, pageable, total);
    }

}
