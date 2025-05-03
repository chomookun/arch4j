package org.chomookun.arch4j.core.notification.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.notification.entity.NotificationEntity;
import org.chomookun.arch4j.core.notification.model.NotificationSearch;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Slf4j
class NotificationRepositoryTest extends CoreTestSupport {

    final NotificationRepository alarmRepository;

    @Test
    void findAll() {
        // given
        NotificationSearch notificationSearch = NotificationSearch.builder()
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        // when
        Page<NotificationEntity> notificationEntityPage = alarmRepository.findAll(notificationSearch, pageable);
        // then
        log.info("{}", notificationEntityPage);
    }

    @Test
    void findAllWithUnPaged() {
        // given
        NotificationSearch notificationSearch = NotificationSearch.builder()
                .build();
        Pageable pageable = Pageable.unpaged();
        // when
        Page<NotificationEntity> notificationEntityPage = alarmRepository.findAll(notificationSearch, pageable);
        // then
        log.info("{}", notificationEntityPage);
    }

}