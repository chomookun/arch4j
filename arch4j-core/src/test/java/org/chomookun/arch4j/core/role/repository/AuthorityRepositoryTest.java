package org.chomookun.arch4j.core.role.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.role.entity.AuthorityEntity;
import org.chomookun.arch4j.core.role.model.AuthoritySearch;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@Slf4j
class AuthorityRepositoryTest extends CoreTestSupport {

    private final AuthorityRepository authorityRepository;

    @Test
    void findAll() {
        // given
        AuthoritySearch authoritySearch = AuthoritySearch.builder()
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        // when
        Page<AuthorityEntity> authorityEntityPage = authorityRepository.findAll(authoritySearch, pageable);
        // then
        log.info("authorityEntityPage: {}", authorityEntityPage);
    }

    @Test
    void findAllWithUnPaged() {
        // given
        AuthoritySearch authoritySearch = AuthoritySearch.builder()
                .build();
        Pageable pageable = Pageable.unpaged();
        // when
        Page<AuthorityEntity> authorityEntityPage = authorityRepository.findAll(authoritySearch, pageable);
        // then
        log.info("authorityEntityPage: {}", authorityEntityPage);
    }

}