package org.chomookun.arch4j.core.notification.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.notification.entity.NotifierEntity;
import org.chomookun.arch4j.core.notification.model.NotifierSearch;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Slf4j
class NotificationRepositoryTest extends CoreTestSupport {

    final NotifierRepository alarmRepository;

    @Test
    void findAll() {
        // given
        NotifierSearch notificationSearch = NotifierSearch.builder()
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        // when
        Page<NotifierEntity> notificationEntityPage = alarmRepository.findAll(notificationSearch, pageable);
        // then
        log.info("{}", notificationEntityPage);
    }

    @Test
    void findAllWithUnPaged() {
        // given
        NotifierSearch notificationSearch = NotifierSearch.builder()
                .build();
        Pageable pageable = Pageable.unpaged();
        // when
        Page<NotifierEntity> notificationEntityPage = alarmRepository.findAll(notificationSearch, pageable);
        // then
        log.info("{}", notificationEntityPage);
    }

}