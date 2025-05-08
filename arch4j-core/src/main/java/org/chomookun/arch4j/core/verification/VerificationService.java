package org.chomookun.arch4j.core.verification;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.user.UserService;
import org.chomookun.arch4j.core.user.model.User;
import org.chomookun.arch4j.core.verification.entity.VerificationEntity;
import org.chomookun.arch4j.core.verification.entity.VerifierEntity;
import org.chomookun.arch4j.core.verification.model.*;
import org.chomookun.arch4j.core.verification.repository.VerificationRepository;
import org.chomookun.arch4j.core.verification.repository.VerifierRepository;
import org.chomookun.arch4j.core.verification.client.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerifierRepository verifierRepository;

    private final VerificationRepository verificationRepository;

    private final UserService userService;

    public List<Map<String,String>> getAvailableVerifiers() {
        return verifierRepository.findAll().stream()
                .filter(VerifierEntity::isEnabled)
                .map(it ->
                        Map.of(
                                "verifierId", it.getVerifierId(),
                                "name", it.getName()
                        ))
                .toList();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public IssueChallengeResult issueChallenge(IssueChallengeParam param) {
        VerifierEntity verifierEntity = verifierRepository.findById(param.getVerifierId()).orElseThrow();
        Verifier verifier = Verifier.from(verifierEntity);
        Optional<User> user = Optional.ofNullable(param.getUserId())
                .flatMap(userService::getUser);

        // saves
        VerificationEntity verificationEntity = VerificationEntity.builder()
                .verificationId(IdGenerator.uuid())
                .issuedAt(Instant.now())
                .verifierId(verifier.getVerifierId())
                .verifierName(verifier.getName())
                .principal(param.getPrincipal())
                .reason(param.getReason())
                .userId(param.getUserId())
                .userName(user.map(User::getName).orElse(null))
                .tryCount(0)
                .build();
        verificationRepository.save(verificationEntity);

        // calls verifier
        Verification verification = Verification.from(verificationEntity);
        VerifierClientDefinition verifierClientDefinition = VerifierClientDefinitionRegistry.getDefinition(verifierEntity.getClientType()).orElseThrow();
        VerifierClientFactory verifierProcessorFactory = VerifierClientFactoryRegistry.getFactory(verifierClientDefinition).orElseThrow();
        VerifierClient verifierProcessor = verifierProcessorFactory.getObject(verifier);
        IssueChallengeResult result = verifierProcessor.issueChallenge(param, verification);

        // updates verification issue
        verificationEntity.setCode(result.getCode());
        verificationEntity.setNotificationId(result.getNotificationId());
        verificationRepository.save(verificationEntity);

        // return
        result.setVerificationId(verificationEntity.getVerificationId());
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public VerifyChallengeResult verifyChallenge(VerifyChallengeParam param) {
        VerificationEntity verificationEntity = verificationRepository.findById(param.getVerificationId()).orElseThrow();
        verificationEntity.setTryCount(verificationEntity.getTryCount() + 1);
        verificationEntity.setTryAt(Instant.now());
        Verification.Result result = null;

        // checks try count
        if (verificationEntity.getTryCount() > 5) {
            result = Verification.Result.TOO_MANY_TRIES;
        }

        // checks expired
        Instant expiredAt = verificationEntity.getIssuedAt().plusSeconds(60 * 5);
        if (Instant.now().isAfter(expiredAt)) {
            result = Verification.Result.EXPIRED;
        }

        // calls verifier
        if (result == null) {
            Verification verification = Verification.from(verificationEntity);
            VerifierEntity verifierEntity = verifierRepository.findById(verificationEntity.getVerifierId()).orElseThrow();
            Verifier verifier = Verifier.from(verifierEntity);
            VerifierClientDefinition verifierClientDefinition = VerifierClientDefinitionRegistry.getDefinition(verifierEntity.getClientType()).orElseThrow();
            VerifierClientFactory verifierClientFactory = VerifierClientFactoryRegistry.getFactory(verifierClientDefinition).orElseThrow();
            VerifierClient verifierClient = verifierClientFactory.getObject(verifier);
            VerifyChallengeResult clientResult = verifierClient.verifyChallenge(param, verification);
            result = clientResult.getResult();
        }

        // updates verification issue
        verificationEntity.setResult(result);
        verificationRepository.save(verificationEntity);

        // returns
        return VerifyChallengeResult.builder()
                .result(result)
                .build();
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
