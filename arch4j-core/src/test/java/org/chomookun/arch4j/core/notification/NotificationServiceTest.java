package org.chomookun.arch4j.core.notification;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.notification.entity.NotificationEntity;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.notification.model.Notification;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class NotificationServiceTest extends CoreTestSupport {

    private final NotificationService notificationService;

    @Test
    @Order(1)
    void saveToPersist() {
        // given
        Notification notification = Notification.builder()
                .notificationId(IdGenerator.uuid())
                .name("test")
                .build();
        // when
        Notification savedNotification = notificationService.saveNotification(notification);
        // then
        assertNotNull(entityManager.find(NotificationEntity.class, savedNotification.getNotificationId()));
    }

    @Test
    @Order(2)
    void saveToMerge() {
        // given
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .notificationId(IdGenerator.uuid())
                .name("test")
                .build();
        entityManager.persist(notificationEntity);
        // when
        Notification notification = Notification.builder()
                .notificationId(notificationEntity.getNotificationId())
                .name("changed")
                .build();
        notificationService.saveNotification(notification);
        // then
        assertEquals(
                "changed",
                entityManager.find(NotificationEntity.class, notification.getNotificationId())
                        .getName()
        );
    }

    @Test
    @Order(3)
    void getAlarm() {
        // given
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .notificationId("test_alarm")
                .name("test alarm")
                .build();
        entityManager.persist(notificationEntity);
        // when
        Notification notification = notificationService.getNotification(notificationEntity.getNotificationId()).orElse(null);
        //then
        assertNotNull(notification);
    }

    @Test
    @Order(4)
    void deleteAlarm() {
        // given
        NotificationEntity alarmEntity = NotificationEntity.builder()
                .notificationId("test_alarm")
                .name("test alarm")
                .build();
        entityManager.persist(alarmEntity);
        // when
        notificationService.deleteNotification(alarmEntity.getNotificationId());
        //then
        assertNull(entityManager.find(NotificationEntity.class, alarmEntity.getNotificationId()));
    }

}