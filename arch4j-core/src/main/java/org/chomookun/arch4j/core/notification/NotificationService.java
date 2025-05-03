package org.chomookun.arch4j.core.notification;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.pbe.PbePropertiesUtil;
import org.chomookun.arch4j.core.notification.client.NotificationClient;
import org.chomookun.arch4j.core.notification.client.NotificationClientFactory;
import org.chomookun.arch4j.core.notification.entity.NotificationEntity;
import org.chomookun.arch4j.core.notification.repository.NotificationRepository;
import org.chomookun.arch4j.core.notification.model.Notification;
import org.chomookun.arch4j.core.notification.model.NotificationSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final NotificationRepository notificationRepository;

    /**
     * Saves notification
     * @param notification notification
     * @return saved notification
     */
    @Transactional
    public Notification saveNotification(Notification notification) {
        NotificationEntity notificationEntity = Optional.ofNullable(notification.getNotificationId())
                .flatMap(notificationRepository::findById)
                .orElse(NotificationEntity.builder()
                        .notificationId(notification.getNotificationId())
                        .build());
        notificationEntity.setSystemUpdatedAt(LocalDateTime.now());
        notificationEntity.setName(notification.getName());
        notificationEntity.setClientId(notification.getClientId());
        notificationEntity.setClientConfig(PbePropertiesUtil.encodePropertiesString(notification.getClientConfig()));
        // saves
        NotificationEntity savedNotificationEntity = notificationRepository.saveAndFlush(notificationEntity);
        entityManager.refresh(savedNotificationEntity);
        return Notification.from(savedNotificationEntity);
    }

    /**
     * Gets notification
     * @param notificationId notification id
     * @return notification
     */
    public Optional<Notification> getNotification(String notificationId) {
        return notificationRepository.findById(notificationId)
                .map(Notification::from);
    }

    /**
     * Deletes alarm
     * @param notificationId alarm id
     */
    @Transactional
    public void deleteNotification(String notificationId) {
        NotificationEntity notificationEntity = notificationRepository.findById(notificationId).orElseThrow();
        notificationRepository.delete(notificationEntity);
        notificationRepository.flush();
    }

    /**
     * Gets alarms
     * @param notificationSearch notification search
     * @param pageable pageable
     * @return page of notification
     */
    public Page<Notification> getNotifications(NotificationSearch notificationSearch, Pageable pageable) {
        Page<NotificationEntity> page = notificationRepository.findAll(notificationSearch, pageable);
        List<Notification> notifications = page.getContent().stream()
                .map(Notification::from)
                .collect(Collectors.toList());
        return new PageImpl<>(notifications, pageable, page.getTotalElements());
    }

    /**
     * Tests alarm
     * @param notification alarm
     */
    public void testNotification(Notification notification, String to, String subject, String content) {
        NotificationClient notificationClient = NotificationClientFactory.getNotificationClient(notification);
        notificationClient.sendMessage(to, subject, content, null);
    }

}
