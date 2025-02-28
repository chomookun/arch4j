package org.chomookun.arch4j.core.email.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;

import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "core_email_verification")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailVerificationEntity extends BaseEntity {

    @Id
    @Column(name = "email", length = 64)
    @Comment("Email")
    private String email;

    @Column(name = "issued_at")
    @Comment("Issued At")
    private LocalDateTime issuedAt;

    @Column(name = "answer", length = 32)
    @Comment("Answer")
    private String answer;

}
