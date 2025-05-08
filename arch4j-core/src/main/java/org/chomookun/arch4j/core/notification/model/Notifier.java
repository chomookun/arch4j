package org.chomookun.arch4j.core.notification.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.notification.entity.NotifierEntity;
import org.chomookun.arch4j.core.common.data.BaseModel;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notifier extends BaseModel {

    private String notifierId;

    private String name;

    private String clientType;

    private String clientProperties;

    /**
     * Notification factory method
     * @param notificationEntity notification entity
     * @return notification
     */
    public static Notifier from(NotifierEntity notificationEntity) {
        return Notifier.builder()
                .systemRequired(notificationEntity.isSystemRequired())
                .systemUpdatedAt(notificationEntity.getSystemUpdatedAt())
                .systemUpdatedBy(notificationEntity.getSystemUpdatedBy())
                .notifierId(notificationEntity.getNotifierId())
                .name(notificationEntity.getName())
                .clientType(notificationEntity.getClientType())
                .clientProperties(notificationEntity.getClientProperties())
                .build();
    }

}
