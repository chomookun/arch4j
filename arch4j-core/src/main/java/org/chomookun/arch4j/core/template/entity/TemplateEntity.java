package org.chomookun.arch4j.core.template.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.i18n.I18nGetter;
import org.chomookun.arch4j.core.common.i18n.I18nSetter;
import org.chomookun.arch4j.core.common.i18n.I18nSupportEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "core_template")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TemplateEntity extends BaseEntity implements I18nSupportEntity<TemplateI18nEntity> {

    @Id
    @Column(name = "template_id", length = 32)
    private String templateId;

    @Column(name = "name")
    private String name;

    @Column(name = "subject", length = 1024)
    private String subject;

    @Column(name = "content", length = Integer.MAX_VALUE)
    @Lob
    private String content;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "template_id", updatable = false)
    @Builder.Default
    private List<TemplateI18nEntity> i18ns = new ArrayList<>();

    @Override
    public List<TemplateI18nEntity> provideI18nEntities() {
        return this.i18ns;
    }

    @Override
    public TemplateI18nEntity provideNewI18nEntity(String language) {
        return TemplateI18nEntity.builder()
                .templateId(this.templateId)
                .language(language)
                .build();
    }

    public void setSubject(String subject) {
        I18nSetter.of(this, this.subject)
                .whenDefault(() -> this.subject = subject)
                .whenI18n(emailLanguageEntity -> emailLanguageEntity.setSubject(subject))
                .set();
    }

    public String getSubject() {
        return I18nGetter.of(this, this.subject)
                .whenDefault(() -> this.subject)
                .whenI18n(TemplateI18nEntity::getSubject)
                .get();
    }

    public void setContent(String content) {
        I18nSetter.of(this, this.content)
                .whenDefault(() -> this.content = content)
                .whenI18n(emailLanguageEntity -> emailLanguageEntity.setContent(content))
                .set();
    }

    public String getContent() {
        return I18nGetter.of(this, this.content)
                .whenDefault(() -> this.content)
                .whenI18n(TemplateI18nEntity::getContent)
                .get();
    }

}
