package org.chomookun.arch4j.core.user.model;

import lombok.Builder;
import lombok.Getter;
import org.chomookun.arch4j.core.user.entity.UserCredentialEntity;

import java.time.Instant;

@Builder
@Getter
public class UserCredential {

    private String userId;

    private UserCredential.Type type;

    private String credential;

    private Instant changedAt;

    public enum Type {
        PASSWORD,
        TOTP
    }

    /**
     * Factory method
     * @param userCredentialEntity user credential entity
     * @return user credential
     */
    public static UserCredential from(UserCredentialEntity userCredentialEntity) {
        return UserCredential.builder()
                .userId(userCredentialEntity.getUserId())
                .type(userCredentialEntity.getType())
                .credential(userCredentialEntity.getCredential())
                .changedAt(userCredentialEntity.getChangedAt())
                .build();
    }

}
