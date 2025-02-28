package org.chomookun.arch4j.core.variable.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.chomookun.arch4j.core.variable.entity.VariableEntity;
import org.chomookun.arch4j.core.variable.model.VariableSearch;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Slf4j
class VariableRepositoryTest extends CoreTestSupport {

    final VariableRepository variableRepository;

    @Test
    void findAll() {
        // given
        VariableSearch variableSearch = VariableSearch.builder()
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        // when
        Page<VariableEntity> authorityEntityPage = variableRepository.findAll(variableSearch, pageable);
        // then
        log.info("variableEntityPage: {}", authorityEntityPage);
    }

    @Test
    void findAllWithUnPaged() {
        // given
        VariableSearch variableSearch = VariableSearch.builder()
                .build();
        Pageable pageable = Pageable.unpaged();
        // when
        Page<VariableEntity> variableEntityPage = variableRepository.findAll(variableSearch, pageable);
        // then
        log.info("variableEntityPage: {}", variableEntityPage);
    }

}