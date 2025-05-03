package org.chomookun.arch4j.core.notification.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.notification.entity.NotificationEntity;
import org.chomookun.arch4j.core.common.data.BaseModel;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notification extends BaseModel {

    private String notificationId;

    private String name;

    private String clientId;

    private String clientConfig;

    /**
     * Notification factory method
     * @param notificationEntity notification entity
     * @return notification
     */
    public static Notification from(NotificationEntity notificationEntity) {
        return Notification.builder()
                .systemRequired(notificationEntity.isSystemRequired())
                .systemUpdatedAt(notificationEntity.getSystemUpdatedAt())
                .systemUpdatedBy(notificationEntity.getSystemUpdatedBy())
                .notificationId(notificationEntity.getNotificationId())
                .name(notificationEntity.getName())
                .clientId(notificationEntity.getClientId())
                .clientConfig(notificationEntity.getClientConfig())
                .build();
    }

}
