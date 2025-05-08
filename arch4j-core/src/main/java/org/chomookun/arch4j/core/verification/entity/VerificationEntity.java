package org.chomookun.arch4j.core.verification.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.verification.model.Verification;

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

    @Column(name = "issued_at")
    private Instant issuedAt;

    @Column(name = "verifier_id", length = 32)
    private String verifierId;

    @Column(name = "verifier_name")
    private String verifierName;

    @Column(name = "principal")
    private String principal;

    @Column(name = "reason")
    private String reason;

    @Column(name = "user_id", length = 32)
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "code", length = 32)
    private String code;

    @Column(name = "notification_id", length = 32)
    private String notificationId;

    @Column(name = "try_count")
    private Integer tryCount;

    @Column(name = "try_at")
    private Instant tryAt;

    @Column(name = "result")
    private Verification.Result result;

}
