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

    private String subject;

    private String content;

    @Builder.Default
    private Map<String,Object> variables = new LinkedHashMap<>();

    /**
     * Adds variable to the email template
     * @param key key
     * @param value value
     */
    public void addVariable(String key, Object value) {
        variables.put(key, value);
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
                .subject(templateEntity.getSubject())
                .content(templateEntity.getContent())
                .build();
    }

}
