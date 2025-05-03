package org.chomookun.arch4j.core.notification.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.chomookun.arch4j.core.notification.entity.NotificationMessageEntity;

import java.time.Instant;
import java.util.Map;

@Builder
@Getter
@Setter
public class NotificationMessage {

    private String messageId;

    private String notificationId;

    private String notificationName;

    private String to;

    private String subject;

    private String content;

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

    public static NotificationMessage from(NotificationMessageEntity alarmMessageEntity) {
        return NotificationMessage.builder()
                .messageId(alarmMessageEntity.getMessageId())
                .notificationId(alarmMessageEntity.getNotificationId())
                .notificationName(alarmMessageEntity.getNotificationName())
                .subject(alarmMessageEntity.getSubject())
                .content(alarmMessageEntity.getContent())
                .submittedAt(alarmMessageEntity.getSubmittedAt())
                .sentAt(alarmMessageEntity.getSentAt())
                .status(alarmMessageEntity.getStatus())
                .errorMessage(alarmMessageEntity.getErrorMessage())
                .build();
    }

}
