package org.chomookun.arch4j.core.verification.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.chomookun.arch4j.core.verification.entity.VerificationEntity;

import java.time.Instant;

@Builder
@Getter
public class Verification {

    private String verificationId;

    private Instant issuedAt;

    private String verifierId;

    private String verifierName;

    private String principal;

    private String reason;

    private String userId;

    private String userName;

    @Setter
    private String code;

    @Setter
    private String notificationId;

    private Integer tryCount;

    private Instant tryAt;

    private Result result;

    public enum Result {
        SUCCESS,
        INVALID_CODE,
        EXPIRED,
        TOO_MANY_TRIES,
    }

    public static Verification from(VerificationEntity verificationEntity) {
        return Verification.builder()
                .verificationId(verificationEntity.getVerificationId())
                .issuedAt(verificationEntity.getIssuedAt())
                .verifierId(verificationEntity.getVerifierId())
                .verifierName(verificationEntity.getVerifierName())
                .principal(verificationEntity.getPrincipal())
                .reason(verificationEntity.getReason())
                .userId(verificationEntity.getUserId())
                .userName(verificationEntity.getUserName())
                .code(verificationEntity.getCode())
                .notificationId(verificationEntity.getNotificationId())
                .tryCount(verificationEntity.getTryCount())
                .tryAt(verificationEntity.getTryAt())
                .result(verificationEntity.getResult())
                .build();
    }

}
