package org.chomookun.arch4j.core.verification.model;

import lombok.Builder;
import lombok.Getter;
import org.chomookun.arch4j.core.verification.entity.VerificationEntity;

@Builder
@Getter
public class Verification {

    private String verificationId;

    private String verifierType;

    private String verifierConfig;

    private boolean enabled;

    /**
     * Factory method
     * @param verificationEntity verification entity
     * @return verification
     */
    public static Verification from(VerificationEntity verificationEntity) {
        return Verification.builder()
                .verificationId(verificationEntity.getVerificationId())
                .verifierType(verificationEntity.getVerifierType())
                .verifierConfig(verificationEntity.getVerifierConfig())
                .enabled(verificationEntity.isEnabled())
                .build();
    }

}
