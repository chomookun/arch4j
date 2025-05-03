package org.chomookun.arch4j.core.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.notification.entity.NotificationMessageEntity;
import org.chomookun.arch4j.core.notification.model.Notification;
import org.chomookun.arch4j.core.notification.model.NotificationMessage;
import org.chomookun.arch4j.core.notification.model.NotificationMessageSearch;
import org.chomookun.arch4j.core.notification.repository.NotificationMessageRepository;
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
public class NotificationMessageService {

    private final NotificationService notificationService;

    private final NotificationMessageConsumer notificationMessageConsumer;
    private final NotificationMessageRepository notificationMessageRepository;

    /**
     * Sends notification message
     * @param notification notification
     * @param to to
     * @param subject subject
     * @param content content
     * @param option option
     * @return notification message
     */
    public NotificationMessage sendNotificationMessage(Notification notification, String to, String subject, String content, Map<String,Object> option) {
        NotificationMessage notificationMessage = NotificationMessage.builder()
                .messageId(IdGenerator.uuid())
                .notificationId(notification.getNotificationId())
                .notificationName(notification.getName())
                .to(to)
                .subject(subject)
                .content(content)
                .option(option)
                .build();
        notificationMessageConsumer.addNotificationMessage(notificationMessage);
        return notificationMessage;
    }

    /**
     * Sends notification message
     * @param notificationId notification id
     * @param to to
     * @param subject subject
     * @param content content
     * @param option option
     * @return notification message
     */
    public NotificationMessage sendNotificationMessage(String notificationId, String to, String subject, String content, Map<String,Object> option) {
        Notification notification = notificationService.getNotification(notificationId).orElseThrow();
        return sendNotificationMessage(notification, to, subject, content, option);
    }

    /**
     * Gets notification message
     * @param notificationMessageSearch notification message search
     * @param pageable pageable
     * @return notification message page
     */
    public Page<NotificationMessage> getNotificationMessages(NotificationMessageSearch notificationMessageSearch, Pageable pageable) {
        Page<NotificationMessageEntity> notificationMessageEntityPage = notificationMessageRepository.findAll(notificationMessageSearch, pageable);
        List<NotificationMessage> notificationMessages = notificationMessageEntityPage.getContent().stream()
                .map(NotificationMessage::from)
                .toList();
        long total = notificationMessageEntityPage.getTotalElements();
        return new PageImpl<>(notificationMessages, pageable, total);
    }

}
