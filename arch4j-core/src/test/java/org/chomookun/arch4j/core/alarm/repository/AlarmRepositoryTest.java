package org.chomookun.arch4j.core.alarm.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.alarm.entity.AlarmEntity;
import org.chomookun.arch4j.core.alarm.model.AlarmSearch;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Slf4j
class AlarmRepositoryTest extends CoreTestSupport {

    final AlarmRepository alarmRepository;

    @Test
    void findAll() {
        // given
        AlarmSearch alarmSearch = AlarmSearch.builder()
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        // when
        Page<AlarmEntity> alarmEntityPage = alarmRepository.findAll(alarmSearch, pageable);
        // then
        log.info("alarmEntityPage: {}", alarmEntityPage);
    }

    @Test
    void findAllWithUnPaged() {
        // given
        AlarmSearch alarmSearch = AlarmSearch.builder()
                .build();
        Pageable pageable = Pageable.unpaged();
        // when
        Page<AlarmEntity> alarmEntityPage = alarmRepository.findAll(alarmSearch, pageable);
        // then
        log.info("codeEntityPage: {}", alarmEntityPage);
    }

}