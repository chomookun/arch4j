package org.chomookun.arch4j.core.role.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.security.entity.RoleEntity;
import org.chomookun.arch4j.core.security.model.RoleSearch;
import org.chomookun.arch4j.core.security.repository.RoleRepository;
import org.junit.jupiter.api.*;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Slf4j
class RoleRepositoryTest extends CoreTestSupport {

    private final RoleRepository roleRepository;

    @Test
    void findAll() {
        // given
        RoleSearch roleSearch = RoleSearch.builder()
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        // when
        Page<RoleEntity> roleEntityPage = roleRepository.findAll(roleSearch, pageable);
        // then
        log.info("roleEntityPage: {}", roleEntityPage);
    }

    @Test
    void findAllWithUnPaged() {
        // given
        RoleSearch roleSearch = RoleSearch.builder()
                .build();
        Pageable pageable = Pageable.unpaged();
        // when
        Page<RoleEntity> roleEntityPage = roleRepository.findAll(roleSearch, pageable);
        // then
        log.info("roleEntityPage: {}", roleEntityPage);
    }

}