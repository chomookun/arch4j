package org.chomookun.arch4j.core.notification;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.notification.entity.NotifierEntity;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.notification.model.Notifier;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class NotificationServiceTest extends CoreTestSupport {

    private final NotifierService notificationService;

    @Test
    @Order(1)
    void saveToPersist() {
        // given
        Notifier notification = Notifier.builder()
                .notifierId(IdGenerator.uuid())
                .name("test")
                .build();
        // when
        Notifier savedNotification = notificationService.saveNotifier(notification);
        // then
        assertNotNull(entityManager.find(NotifierEntity.class, savedNotification.getNotifierId()));
    }

    @Test
    @Order(2)
    void saveToMerge() {
        // given
        NotifierEntity notificationEntity = NotifierEntity.builder()
                .notifierId(IdGenerator.uuid())
                .name("test")
                .build();
        entityManager.persist(notificationEntity);
        // when
        Notifier notification = Notifier.builder()
                .notifierId(notificationEntity.getNotifierId())
                .name("changed")
                .build();
        notificationService.saveNotifier(notification);
        // then
        assertEquals(
                "changed",
                entityManager.find(NotifierEntity.class, notification.getNotifierId())
                        .getName()
        );
    }

    @Test
    @Order(3)
    void getAlarm() {
        // given
        NotifierEntity notificationEntity = NotifierEntity.builder()
                .notifierId("test_alarm")
                .name("test alarm")
                .build();
        entityManager.persist(notificationEntity);
        // when
        Notifier notification = notificationService.getNotifier(notificationEntity.getNotifierId()).orElse(null);
        //then
        assertNotNull(notification);
    }

    @Test
    @Order(4)
    void deleteAlarm() {
        // given
        NotifierEntity alarmEntity = NotifierEntity.builder()
                .notifierId("test_alarm")
                .name("test alarm")
                .build();
        entityManager.persist(alarmEntity);
        // when
        notificationService.deleteNotifier(alarmEntity.getNotifierId());
        //then
        assertNull(entityManager.find(NotifierEntity.class, alarmEntity.getNotifierId()));
    }

}