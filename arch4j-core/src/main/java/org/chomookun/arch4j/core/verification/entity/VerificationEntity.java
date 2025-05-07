package org.chomookun.arch4j.core.verification.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.notification.entity.NotificationEntity;
import org.chomookun.arch4j.core.user.entity.UserEntity;

import java.time.Instant;

@Entity
@Table(name = "core_verification")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VerificationEntity extends BaseEntity {

    @Id
    @Column(name = "verification_id", length = 32)
    private String verificationId;

    @Column(name = "verifier_id", length = 32)
    private String verifierId;

    @Column(name = "principal")
    private String principal;

    @Column(name = "reason")
    private String reason;

    @Column(name = "user_id", length = 32)
    private String userId;

    @Column(name = "notification_message_id", length = 32)
    private String notificationMessageId;

    @Column(name = "code", length = 32)
    private String code;

    @Column(name = "issued_at")
    private Instant issuedAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "try_count")
    private Integer tryCount;

    @Column(name = "verified", length = 1)
    private boolean verified;

    @Column(name = "verified_at")
    private Instant verifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_message_id", insertable = false, updatable = false)
    private NotificationEntity notificationMessage;

}
