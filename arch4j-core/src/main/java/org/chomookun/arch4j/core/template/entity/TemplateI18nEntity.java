package org.chomookun.arch4j.core.template.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.i18n.I18nEntity;

import java.io.Serializable;

@Entity
@Table(name = "core_template_i18n")
@IdClass(TemplateI18nEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TemplateI18nEntity extends BaseEntity implements I18nEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String templateId;
        private String language;
    }

    @Id
    @Column(name = "template_id", length = 32)
    private String templateId;

    @Id
    @Column(name = "language", length = 8)
    private String language;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content", length = Integer.MAX_VALUE)
    @Lob
    private String content;

}

