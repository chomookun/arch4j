package org.chomookun.arch4j.core.notification.model;

import lombok.*;
import org.chomookun.arch4j.core.notification.entity.NotificationEntity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
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

    private boolean suppress;

    private Map<String, Object> option;

    private Instant submittedAt;

    private Instant sentAt;

    private Status status;

    private String errorMessage;

    public String getSuppressedKey() {
        String rawKey = String.join(":",
                this.notifierId,
                this.subject,
                this.content,
                this.receiver
        );
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(rawKey.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public enum Status {
        SUBMITTED,
        COMPLETED,
        FAILED,
        SUPPRESSED
    }

    public static Notification from(NotificationEntity notificationEntity) {
        return Notification.builder()
                .notificationId(notificationEntity.getNotificationId())
                .notifierId(notificationEntity.getNotifierId())
                .notifierName(notificationEntity.getNotifierName())
                .receiver(notificationEntity.getReceiver())
                .suppress(notificationEntity.isSuppress())
                .subject(notificationEntity.getSubject())
                .content(notificationEntity.getContent())
                .submittedAt(notificationEntity.getSubmittedAt())
                .sentAt(notificationEntity.getSentAt())
                .status(notificationEntity.getStatus())
                .errorMessage(notificationEntity.getErrorMessage())
                .build();
    }

}
