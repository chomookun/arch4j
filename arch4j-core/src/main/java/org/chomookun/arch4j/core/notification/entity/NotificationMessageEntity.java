package org.chomookun.arch4j.core.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.notification.model.NotificationMessage;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.data.converter.GenericEnumConverter;

import java.time.Instant;

@Entity
@Table(name = "core_notifiction_message")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotificationMessageEntity extends BaseEntity {

    @Id
    @Column(name = "message_id")
    private String messageId;

    @Column(name = "notification_id")
    private String notificationId;

    @Column(name = "notification_name")
    private String notificationName;

    @Column(name = "to")
    private String to;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content", length = Integer.MAX_VALUE)
    @Lob
    private String content;

    @Column(name = "submitted_at")
    private Instant submittedAt;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "status", length = 16)
    private NotificationMessage.Status status;

    @Column(name = "error_message", length = Integer.MAX_VALUE)
    @Lob
    private String errorMessage;

    @Converter(autoApply = true)
    public static class StatusConverter extends GenericEnumConverter<NotificationMessage.Status> { }

}
