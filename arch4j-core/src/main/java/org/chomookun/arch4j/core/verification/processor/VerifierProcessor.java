package org.chomookun.arch4j.core.verification.processor;

import lombok.Getter;
import org.chomookun.arch4j.core.verification.model.*;

import java.security.SecureRandom;
import java.util.Properties;

public abstract class VerifierProcessor {

    @Getter
    private final Properties config;

    protected VerifierProcessor(Properties config) {
        this.config = config;
    }

    public abstract IssueChallengeResult issueChallenge(IssueChallengeParam param, Verification verification);

    public abstract VerifyChallengeResult verifyChallenge(VerifyChallengeParam param, Verification verification);

    protected String generateCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1_000_000));
    }

}
