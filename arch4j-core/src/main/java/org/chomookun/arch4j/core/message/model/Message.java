package org.chomookun.arch4j.core.message.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseModel;
import org.chomookun.arch4j.core.common.i18n.I18nSupport;
import org.chomookun.arch4j.core.message.entity.MessageEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Message extends BaseModel implements I18nSupport<MessageI18n> {

    @NotNull
    private String messageId;

    @NotNull
    private String name;

    /**
     * Sets localized value
     * @param value value
     */
    public void setValue(String value) {
        i18nSet(i18n -> i18n.setValue(value));
    }

    /**
     * Gets localized value
     * @return localized value
     */
    public String getValue() {
        return i18nGet(MessageI18n::getValue);
    }

    private String note;

    @Builder.Default
    @JsonIgnore
    private List<MessageI18n> i18ns = new ArrayList<>();

    @Override
    public MessageI18n provideNewI18n(String locale) {
        return MessageI18n.builder()
                .messageId(this.messageId)
                .locale(locale)
                .build();
    }

    /**
     * factory method
     * @param messageEntity message entity
     * @return message
     */
    public static Message from(MessageEntity messageEntity) {
        return Message.builder()
                .systemRequired(messageEntity.isSystemRequired())
                .systemUpdatedAt(messageEntity.getSystemUpdatedAt())
                .systemUpdatedBy(messageEntity.getSystemUpdatedBy())
                .messageId(messageEntity.getMessageId())
                .name(messageEntity.getName())
                .i18ns(messageEntity.getI18ns().stream()
                        .map(MessageI18n::from)
                        .toList())
                .note(messageEntity.getNote())
                .build();
    }

}
