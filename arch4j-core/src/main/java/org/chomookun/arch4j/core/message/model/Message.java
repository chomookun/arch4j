package org.chomookun.arch4j.core.message.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseModel;
import org.chomookun.arch4j.core.message.entity.MessageEntity;
import org.jetbrains.annotations.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Message extends BaseModel {

    @NotNull
    private String messageId;

    @NotBlank
    private String name;

    private String value;

    private String note;

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
                .value(messageEntity.getValue())
                .note(messageEntity.getNote())
                .build();
    }

}
