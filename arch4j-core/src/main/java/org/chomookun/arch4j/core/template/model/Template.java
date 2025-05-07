package org.chomookun.arch4j.core.template.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseModel;
import org.chomookun.arch4j.core.email.entity.EmailEntity;
import org.chomookun.arch4j.core.template.entity.TemplateEntity;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Template extends BaseModel {

    private String templateId;

    private String name;

    private Format format;

    private String subject;

    private String content;

    public enum Format {
        TEXT,
        HTML
    }

    /**
     * factory method
     * @param templateEntity template entity
     * @return template
     */
    public static Template from(TemplateEntity templateEntity) {
        return Template.builder()
                .systemRequired(templateEntity.isSystemRequired())
                .systemUpdatedAt(templateEntity.getSystemUpdatedAt())
                .systemUpdatedBy(templateEntity.getSystemUpdatedBy())
                .templateId(templateEntity.getTemplateId())
                .name(templateEntity.getName())
                .format(templateEntity.getFormat())
                .subject(templateEntity.getSubject())
                .content(templateEntity.getContent())
                .build();
    }

}
