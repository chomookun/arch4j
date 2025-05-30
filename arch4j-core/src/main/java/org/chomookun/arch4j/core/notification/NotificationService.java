package org.chomookun.arch4j.core.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.notification.entity.NotificationEntity;
import org.chomookun.arch4j.core.notification.model.Notification;
import org.chomookun.arch4j.core.notification.model.Notifier;
import org.chomookun.arch4j.core.notification.model.NotificationSearch;
import org.chomookun.arch4j.core.notification.repository.NotificationRepository;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Lazy(false)
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotifierService notifierService;

    private final NotificationConsumer notificationConsumer;

    private final NotificationRepository notificationRepository;

    /**
     * Sends notification
     * @param notifier notifier
     * @param subject subject
     * @param content content
     * @param receiver receiver
     * @param suppressed whether the notification is suppressed
     * @return notification
     */
    public Notification sendNotification(Notifier notifier, String subject, String content, String receiver, boolean suppressed) {
        Notification notification = Notification.builder()
                .notificationId(IdGenerator.uuid())
                .notifierId(notifier.getNotifierId())
                .notifierName(notifier.getName())
                .subject(subject)
                .content(content)
                .receiver(receiver)
                .suppressed(suppressed)
                .build();
        notificationConsumer.addNotification(notification);
        return notification;
    }

    /**
     * Sends notification
     * @param notifierId notifier id
     * @param receiver receiver
     * @param subject subject
     * @param content content
     * @param suppressed whether the notification is suppressed
     * @return notification
     */
    public Notification sendNotification(String notifierId, String subject, String content, String receiver, boolean suppressed) {
        Notifier notification = notifierService.getNotifier(notifierId).orElseThrow();
        return sendNotification(notification, subject, content, receiver, suppressed);
    }

    /**
     * Gets notifications
     * @param notificationSearch notification search
     * @param pageable pageable
     * @return notification page
     */
    public Page<Notification> getNotifications(NotificationSearch notificationSearch, Pageable pageable) {
        Page<NotificationEntity> notificationEntityPage = notificationRepository.findAll(notificationSearch, pageable);
        List<Notification> notifications = notificationEntityPage.getContent().stream()
                .map(Notification::from)
                .toList();
        long total = notificationEntityPage.getTotalElements();
        return new PageImpl<>(notifications, pageable, total);
    }

}
