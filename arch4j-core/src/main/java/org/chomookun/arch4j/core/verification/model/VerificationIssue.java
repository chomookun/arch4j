package org.chomookun.arch4j.core.verification.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.chomookun.arch4j.core.notification.model.NotificationMessage;
import org.chomookun.arch4j.core.user.model.User;
import org.chomookun.arch4j.core.verification.entity.VerificationChallengeEntity;

import java.time.Instant;
import java.util.Optional;

@Builder
@Getter
public class VerificationIssue {

    private String issueId;

    private String verificationId;

    private String type;

    private String reason;

    private String notificationId;

    private String principal;

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

    public static VerificationIssue from(VerificationChallengeEntity verificationIssueEntity) {
        return VerificationIssue.builder()
                .issueId(verificationIssueEntity.getIssueId())
                .verificationId(verificationIssueEntity.getVerificationId())
                .principal(verificationIssueEntity.getPrincipal())
                .reason(verificationIssueEntity.getReason())
                .userId(verificationIssueEntity.getUserId())
                .notificationMessageId(verificationIssueEntity.getNotificationMessageId())
                .code(verificationIssueEntity.getCode())
                .issuedAt(verificationIssueEntity.getIssuedAt())
                .expiresAt(verificationIssueEntity.getExpiresAt())
                .tryCount(verificationIssueEntity.getTryCount())
                .verified(verificationIssueEntity.isVerified())
                .user(Optional.ofNullable(verificationIssueEntity.getUser())
                        .map(User::from)
                        .orElse(null))
                .notificationMessage(Optional.ofNullable(verificationIssueEntity.getNotificationMessage())
                        .map(NotificationMessage::from)
                        .orElse(null))
                .build();
    }

}
