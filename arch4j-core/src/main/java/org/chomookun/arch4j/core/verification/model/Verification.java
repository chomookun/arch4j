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

    private String type;

    private String reason;

    private String notificationId;

    private String principal;

    private String userId;

    private User user;

    private String notificationMessageId;

    private NotificationMessage notificationMessage;

    private String code;

    private Instant issuedAt;

    private Instant expiresAt;

    private Integer tryCount;

    @Setter
    private boolean verified;

    @Setter
    private Instant verifiedAt;

    /**
     * Factory method
     * @param verificationEntity verification entity
     * @return verification
     */
    public static Verification from(VerificationEntity verificationEntity) {
        return Verification.builder()
                .type(verificationEntity.getType())
                .reason(verificationEntity.getReason())
                .verificationId(verificationEntity.getVerificationId())
                .notificationId(verificationEntity.getNotificationId())
                .principal(verificationEntity.getPrincipal())
                .userId(verificationEntity.getUserId())
                .user(Optional.ofNullable(verificationEntity.getUser())
                        .map(User::from)
                        .orElse(null))
                .notificationMessageId(verificationEntity.getNotificationMessageId())
                .notificationMessage(Optional.ofNullable(verificationEntity.getNotificationMessage())
                        .map(NotificationMessage::from)
                        .orElse(null))
                .code(verificationEntity.getCode())
                .issuedAt(verificationEntity.getIssuedAt())
                .expiresAt(verificationEntity.getExpiresAt())
                .tryCount(verificationEntity.getTryCount())
                .verified(verificationEntity.isVerified())
                .verifiedAt(verificationEntity.getVerifiedAt())
                .build();
    }

}
