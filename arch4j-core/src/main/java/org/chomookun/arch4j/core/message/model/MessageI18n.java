package org.chomookun.arch4j.core.message.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.chomookun.arch4j.core.common.i18n.I18n;
import org.chomookun.arch4j.core.message.entity.MessageI18nEntity;

@Builder
@Getter
public class MessageI18n implements I18n {

    private String messageId;

    private String locale;

    @Setter
    private String value;

    public static MessageI18n from(MessageI18nEntity messageI18nEntity) {
        return MessageI18n.builder()
                .messageId(messageI18nEntity.getMessageId())
                .locale(messageI18nEntity.getLocale())
                .value(messageI18nEntity.getValue())
                .build();
    }

}
