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
import java.util.Map;

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
     * @param to to
     * @param subject subject
     * @param content content
     * @param option option
     * @return notification
     */
    public Notification sendNotification(Notifier notifier, String to, String subject, String content, Map<String,Object> option) {
        Notification notification = Notification.builder()
                .notificationId(IdGenerator.uuid())
                .notifierId(notifier.getNotifierId())
                .notifierName(notifier.getName())
                .to(to)
                .subject(subject)
                .content(content)
                .option(option)
                .build();
        notificationConsumer.addNotification(notification);
        return notification;
    }

    /**
     * Sends notification message
     * @param notifierId notifier id
     * @param to to
     * @param subject subject
     * @param content content
     * @param option option
     * @return notification
     */
    public Notification sendNotification(String notifierId, String to, String subject, String content, Map<String,Object> option) {
        Notifier notification = notifierService.getNotifier(notifierId).orElseThrow();
        return sendNotification(notification, to, subject, content, option);
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
