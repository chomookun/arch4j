package org.chomookun.arch4j.core.message.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.i18n.I18nSupport;
import org.chomookun.arch4j.core.common.i18n.test1.I18nEntitySupport;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "core_message")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageEntity extends BaseEntity implements I18nSupport<MessageI18nEntity> {

    @Id
    @Column(name = "message_id", length = 128)
    private String messageId;

    @Column(name = "name")
    private String name;

    /**
     * Sets message localized message value
     * @param value message value
     */
    public void setValue(String value) {
        i18nSet(i18n -> i18n.setValue(value));
    }

    /**
     * Gets localized message value
     * @return localized message value
     */
    public String getValue() {
        return i18nGet(MessageI18nEntity::getValue);
    }

    @Column(name = "note", length = 4000)
    @Lob
    private String note;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "message_id", updatable = false)
    @Builder.Default
    private List<MessageI18nEntity> i18ns = new ArrayList<>();

    @Override
    public MessageI18nEntity provideNewI18n(String locale) {
        return MessageI18nEntity.builder()
                .messageId(this.messageId)
                .locale(locale)
                .build();
    }

}
