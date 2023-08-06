package org.oopscraft.arch4j.core.message;

import lombok.*;
import org.oopscraft.arch4j.core.message.dao.MessageI18nEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageI18n {

    private String messageId;

    private String language;

    private String value;

    public static MessageI18n from(MessageI18nEntity messageI18nEntity) {
        return MessageI18n.builder()
                .messageId(messageI18nEntity.getMessageId())
                .language(messageI18nEntity.getLanguage())
                .value(messageI18nEntity.getValue())
                .build();
    }

}
