package org.chomookun.arch4j.core.verification.verifier;

import lombok.Getter;
import org.chomookun.arch4j.core.verification.model.VerificationIssue;

import java.security.SecureRandom;
import java.util.Properties;

public abstract class Verifier {

    @Getter
    private final Properties config;

    protected Verifier(Properties config) {
        this.config = config;
    }

    public abstract void issueCode(VerificationIssue verificationIssue);

    public abstract boolean verifyCode(VerificationIssue verificationIssue, String code);

    protected String generateCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1_000_000));
    }

}
