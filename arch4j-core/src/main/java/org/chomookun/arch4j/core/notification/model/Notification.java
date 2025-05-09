package org.chomookun.arch4j.core.notification.model;

import lombok.*;
import org.chomookun.arch4j.core.notification.entity.NotificationEntity;

import java.time.Instant;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notification {

    private String notificationId;

    private String notifierId;

    private String notifierName;

    private String subject;

    private String content;

    private String receiver;

    private Map<String, Object> option;

    private Instant submittedAt;

    private Instant sentAt;

    private Status status;

    private String errorMessage;

    public enum Status {
        SUBMITTED,
        COMPLETED,
        FAILED
    }

    public static Notification from(NotificationEntity notificationEntity) {
        return Notification.builder()
                .notificationId(notificationEntity.getNotificationId())
                .notifierId(notificationEntity.getNotifierId())
                .notifierName(notificationEntity.getNotifierName())
                .receiver(notificationEntity.getReceiver())
                .subject(notificationEntity.getSubject())
                .content(notificationEntity.getContent())
                .submittedAt(notificationEntity.getSubmittedAt())
                .sentAt(notificationEntity.getSentAt())
                .status(notificationEntity.getStatus())
                .errorMessage(notificationEntity.getErrorMessage())
                .build();
    }

}
