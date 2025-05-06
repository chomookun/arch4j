package org.chomookun.arch4j.core.verification.model;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.*;
import org.chomookun.arch4j.core.verification.entity.VerifierEntity;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Verifier {

    private String verifierId;

    private String name;

    private String processorType;

    private String processorConfig;

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
                .processorType(verifierEntity.getProcessorType())
                .processorConfig(verifierEntity.getProcessorConfig())
                .enabled(verifierEntity.isEnabled())
                .note(verifierEntity.getNote())
                .build();
    }

}
