package org.chomookun.arch4j.core.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.role.entity.RoleEntity;
import org.chomookun.arch4j.core.user.entity.UserEntity;
import org.chomookun.arch4j.core.user.entity.UserRoleEntity;
import org.chomookun.arch4j.core.user.model.UserSearch;
import org.chomookun.arch4j.core.user.repository.UserRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@Slf4j
class UserRepositoryTest extends CoreTestSupport {

    final UserRepository userRepository;

    @Test
    void findAll() {
        // given
        UserSearch userSearch = UserSearch.builder()
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        // when
        Page<UserEntity> userEntityPage = userRepository.findAll(userSearch, pageable);
        // then
        log.info("userEntityPage: {}", userEntityPage);
    }

    @Test
    void findAllWithUnPaged() {
        // given
        UserSearch userSearch = UserSearch.builder()
                .build();
        Pageable pageable = Pageable.unpaged();
        // when
        Page<UserEntity> userEntityPage = userRepository.findAll(userSearch, pageable);
        // then
        log.info("userEntityPage: {}", userEntityPage);
    }

}