package org.chomookun.arch4j.core.email.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseModel;
import org.chomookun.arch4j.core.email.entity.EmailEntity;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Email extends BaseModel {

    private String emailId;

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
     * Email factory method
     * @param emailTemplateEntity email template entity
     * @return email
     */
    public static Email from(EmailEntity emailTemplateEntity) {
        return Email.builder()
                .systemRequired(emailTemplateEntity.isSystemRequired())
                .systemUpdatedAt(emailTemplateEntity.getSystemUpdatedAt())
                .systemUpdatedBy(emailTemplateEntity.getSystemUpdatedBy())
                .emailId(emailTemplateEntity.getEmailId())
                .name(emailTemplateEntity.getName())
                .subject(emailTemplateEntity.getSubject())
                .content(emailTemplateEntity.getContent())
                .build();
    }

}
