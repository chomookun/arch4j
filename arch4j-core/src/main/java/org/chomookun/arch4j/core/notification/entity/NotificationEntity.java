package org.chomookun.arch4j.core.notification.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;

import jakarta.persistence.*;

@Entity
@Table(name = "core_notification")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationEntity extends BaseEntity {

    @Id
    @Column(name = "notification_id", length = 32)
    private String notificationId;

    @Column(name = "name")
    private String name;

    @Column(name = "client_id", length = 32)
    private String clientId;

    @Column(name = "client_config", length = Integer.MAX_VALUE)
    @Lob
    private String clientConfig;

}
