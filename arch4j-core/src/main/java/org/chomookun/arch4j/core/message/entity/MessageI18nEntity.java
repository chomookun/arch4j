package org.chomookun.arch4j.core.message.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.i18n.I18n;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_message_i18n")
@IdClass(MessageI18nEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageI18nEntity extends BaseEntity implements I18n {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String messageId;
        private String locale;
    }

    @Id
    @Column(name = "message_id", length = 64)
    private String messageId;

    @Id
    @Column(name = "locale", length = 8)
    private String locale;

    @Column(name = "value", length = 4000)
    @Lob
    private String value;

}

