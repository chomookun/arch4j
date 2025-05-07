package org.chomookun.arch4j.core.verification;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.verification.entity.VerificationEntity;
import org.chomookun.arch4j.core.verification.entity.VerifierEntity;
import org.chomookun.arch4j.core.verification.model.*;
import org.chomookun.arch4j.core.verification.repository.VerificationRepository;
import org.chomookun.arch4j.core.verification.repository.VerifierRepository;
import org.chomookun.arch4j.core.verification.processor.*;
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
public class VerificationService {

    private final VerifierRepository verifierRepository;

    private final VerificationRepository verificationRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public IssueChallengeResult issueChallenge(IssueChallengeParam param) {
        VerifierEntity verifierEntity = verifierRepository.findById(param.getVerifierId()).orElseThrow();
        Verifier verifier = Verifier.from(verifierEntity);

        // saves
        VerificationEntity verificationEntity = VerificationEntity.builder()
                .verificationId(IdGenerator.uuid())
                .verifierId(verifier.getVerifierId())
                .principal(param.getPrincipal())
                .reason(param.getReason())
                .userId(param.getUserId())
                .issuedAt(Instant.now())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60 * 10))
                .tryCount(0)
                .verified(false)
                .verifiedAt(null)
                .build();
        verificationRepository.save(verificationEntity);
        Verification verification = Verification.from(verificationEntity);

        // calls verifier
        VerifierProcessorDefinition verifierProcessorDefinition = VerifierProcessorDefinitionRegistry.getDefinition(verifierEntity.getProcessorType()).orElseThrow();
        VerifierProcessorFactory verifierProcessorFactory = VerifierProcessorFactoryRegistry.getFactory(verifierProcessorDefinition).orElseThrow();
        VerifierProcessor verifierProcessor = verifierProcessorFactory.getObject(verifier);
        IssueChallengeResult result = verifierProcessor.issueChallenge(param, verification);

        // updates verification issue
        verificationEntity.setCode(verification.getCode());
        verificationEntity.setNotificationMessageId(verification.getNotificationMessageId());
        verificationRepository.save(verificationEntity);

        // return
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public VerifyChallengeResult verifyChallenge(VerifyChallengeParam param) {
        VerificationEntity verificationEntity = verificationRepository.findById(param.getVerificationId()).orElseThrow();
        verificationEntity.setTryCount(verificationEntity.getTryCount() + 1);
        Verification verification = Verification.from(verificationEntity);

        VerifierEntity verifierEntity = verifierRepository.findById(verificationEntity.getVerifierId()).orElseThrow();
        Verifier verifier = Verifier.from(verifierEntity);

        // calls verifier
        VerifierProcessorDefinition verifierProcessorDefinition = VerifierProcessorDefinitionRegistry.getDefinition(verifierEntity.getProcessorType()).orElseThrow();
        VerifierProcessorFactory verifierProcessorFactory = VerifierProcessorFactoryRegistry.getFactory(verifierProcessorDefinition).orElseThrow();
        VerifierProcessor verifierProcessor = verifierProcessorFactory.getObject(verifier);
        VerifyChallengeResult result = verifierProcessor.verifyChallenge(param, verification);

        // updates verification issue
        if (result.getResult() == VerifyChallengeResult.Result.SUCCESS) {
            verificationEntity.setVerified(true);
            verificationEntity.setVerifiedAt(Instant.now());
        }
        verificationRepository.save(verificationEntity);
        // returns
        return result;
    }

    public Page<Verification> getVerifications(VerificationSearch verificationSearch, Pageable pageable) {
        Page<VerificationEntity> verificationIssueEntityPage = verificationRepository.findAll(verificationSearch, pageable);
        List<Verification> verificationIssues = verificationIssueEntityPage.getContent().stream()
                .map(Verification::from)
                .toList();
        long total = verificationIssueEntityPage.getTotalElements();
        return new PageImpl<>(verificationIssues, pageable, total);
    }

}
