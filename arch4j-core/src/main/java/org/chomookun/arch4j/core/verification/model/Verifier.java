package org.chomookun.arch4j.core.verification.model;

import lombok.*;
import org.chomookun.arch4j.core.verification.entity.VerifierEntity;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Verifier {

    private String verifierId;

    private String name;

    private String clientType;

    private String clientProperties;

    private boolean enabled;

    private String note;

    /**
     * Factory method
     * @param verifierEntity verifier entity
     * @return verifier
     */
    public static Verifier from(VerifierEntity verifierEntity) {
        return Verifier.builder()
                .verifierId(verifierEntity.getVerifierId())
                .name(verifierEntity.getName())
                .clientType(verifierEntity.getClientType())
                .clientProperties(verifierEntity.getClientProperties())
                .enabled(verifierEntity.isEnabled())
                .note(verifierEntity.getNote())
                .build();
    }

}
