package org.chomookun.arch4j.core.email.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.i18n.I18nEntity;

import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

@Entity
@Table(name = "core_email_i18n")
@IdClass(EmailI18nEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailI18nEntity extends BaseEntity implements I18nEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String emailId;
        private String language;
    }

    @Id
    @Column(name = "email_id", length = 32)
    @Comment("Email ID")
    private String emailId;

    @Id
    @Column(name = "language", length = 8)
    @Comment("Language")
    private String language;

    @Column(name = "subject")
    @Comment("Subject")
    private String subject;

    @Column(name = "content", length = Integer.MAX_VALUE)
    @Lob
    @Comment("Content")
    private String content;

}

