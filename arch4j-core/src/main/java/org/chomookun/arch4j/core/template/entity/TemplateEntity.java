package org.chomookun.arch4j.core.template.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.data.converter.GenericEnumConverter;
import org.chomookun.arch4j.core.common.i18n.I18nSupport;
import org.chomookun.arch4j.core.common.i18n.test1.I18nEntitySupport;
import org.chomookun.arch4j.core.template.model.Template;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "core_template")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TemplateEntity extends BaseEntity implements I18nSupport<TemplateI18nEntity> {

    @Id
    @Column(name = "template_id", length = 32)
    private String templateId;

    @Column(name = "name")
    private String name;

    @Column(name = "format", length = 16)
    @Convert(converter = FormatConverter.class)
    private Template.Format format;

    /**
     * Sets template localized subject
     * @param subject subject
     */
    public void setSubject(String subject) {
        i18nSet(i18n -> i18n.setSubject(subject));
    }

    /**
     * Gets localized value of subject
     * @return localized subject
     */
    public String getSubject() {
        return i18nGet(TemplateI18nEntity::getSubject);
    }

    /**
     * Sets template localized content value
     * @param content content
     */
    public void setContent(String content) {
        i18nSet(i18n -> i18n.setContent(content));
    }

    /**
     * Gets localized value of content
     * @return localized content
     */
    public String getContent() {
        return i18nGet(TemplateI18nEntity::getContent);
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "template_id", updatable = false)
    @Builder.Default
    private List<TemplateI18nEntity> i18ns = new ArrayList<>();

    @Override
    public TemplateI18nEntity provideNewI18n(String locale) {
        return TemplateI18nEntity.builder()
                .templateId(this.templateId)
                .locale(locale)
                .build();
    }

    @Converter(autoApply = true)
    public static class FormatConverter extends GenericEnumConverter<Template.Format> {}

}
