package org.chomookun.arch4j.core.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.user.entity.UserEntity;
import org.chomookun.arch4j.core.user.entity.UserRoleEntity;
import org.chomookun.arch4j.core.user.model.UserSearch;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
@Slf4j
class UserRepositoryTest extends CoreTestSupport {

    final UserRepository userRepository;

    @Test
    void saveForPersist() {
        // when
        UserEntity userEntity = UserEntity.builder()
                .userId(IdGenerator.uuid())
                .username("username")
                .name("test name")
                .build();
        UserRoleEntity userRoleEntity = UserRoleEntity.builder()
                .userId(userEntity.getUserId())
                .roleId("test")
                .build();
        userEntity.getUserRoles().add(userRoleEntity);
        UserEntity savedUserEntity = userRepository.saveAndFlush(userEntity);
        // then
        log.info("savedUserEntity: {}", savedUserEntity);
        assertNotNull(entityManager.find(UserEntity.class, savedUserEntity.getUserId()));
    }

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