package org.chomookun.arch4j.core.verification.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.chomookun.arch4j.core.notification.model.NotificationMessage;
import org.chomookun.arch4j.core.user.model.User;
import org.chomookun.arch4j.core.verification.entity.VerificationEntity;

import java.time.Instant;
import java.util.Optional;

@Builder
@Getter
public class Verification {

    private String verificationId;

    private String verifierId;

    private String principal;

    private String reason;

    private String userId;

    private User user;

    @Setter
    private String notificationMessageId;

    private NotificationMessage notificationMessage;

    @Setter
    private String code;

    private Instant issuedAt;

    private Instant expiresAt;

    private Integer tryCount;

    @Setter
    private boolean verified;

    @Setter
    private Instant verifiedAt;

    private Result result;

    public enum Result {
        ISSUED,
        VERIFIED,
        FAILED
    }

    public static Verification from(VerificationEntity verificationEntity) {
        return Verification.builder()
                .verificationId(verificationEntity.getVerificationId())
                .verifierId(verificationEntity.getVerifierId())
                .principal(verificationEntity.getPrincipal())
                .reason(verificationEntity.getReason())
                .userId(verificationEntity.getUserId())
                .notificationMessageId(verificationEntity.getNotificationMessageId())
                .code(verificationEntity.getCode())
                .issuedAt(verificationEntity.getIssuedAt())
                .expiresAt(verificationEntity.getExpiresAt())
                .tryCount(verificationEntity.getTryCount())
                .verified(verificationEntity.isVerified())
                .user(Optional.ofNullable(verificationEntity.getUser())
                        .map(User::from)
                        .orElse(null))
                .notificationMessage(Optional.ofNullable(verificationEntity.getNotificationMessage())
                        .map(NotificationMessage::from)
                        .orElse(null))
                .build();
    }

}
