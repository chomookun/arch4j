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

    private String note;

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
     * @param emailEntity email template entity
     * @return email
     */
    public static Email from(EmailEntity emailEntity) {
        return Email.builder()
                .systemRequired(emailEntity.isSystemRequired())
                .systemUpdatedAt(emailEntity.getSystemUpdatedAt())
                .systemUpdatedBy(emailEntity.getSystemUpdatedBy())
                .emailId(emailEntity.getEmailId())
                .name(emailEntity.getName())
                .subject(emailEntity.getSubject())
                .content(emailEntity.getContent())
                .note(emailEntity.getNote())
                .build();
    }

}
