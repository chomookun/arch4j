package org.oopscraft.arch4j.core.security.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.oopscraft.arch4j.core.common.test.CoreTestSupport;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class RoleRepositoryTest extends CoreTestSupport {

    private final RoleRepository roleRepository;

    private RoleEntity getTestRoleEntity() {

        RoleEntity roleEntity = RoleEntity.builder()
                .roleId("test-role")
                .name("test role")
                .build();

        // add authority
        Arrays.asList("auth-1","auto-2").forEach(authorityId -> {
            entityManager.persist(AuthorityEntity.builder()
                    .authorityId(authorityId)
                    .name("name of " + authorityId)
                    .build());
            entityManager.flush();

            RoleAuthorityEntity roleAuthorityEntity = RoleAuthorityEntity.builder()
                    .roleId(roleEntity.getRoleId())
                    .authorityId(authorityId)
                    .build();
            roleEntity.getRoleAuthorities()
                    .add(roleAuthorityEntity);
        });
        return roleEntity;
    }

    private RoleEntity saveTestRoleEntity() {
        RoleEntity roleEntity = getTestRoleEntity();
        entityManager.persist(roleEntity);
        entityManager.flush();
        entityManager.clear();
        return roleEntity;
    }

    @Test
    @Order(1)
    void saveToPersist() {
        // given
        RoleEntity testRoleEntity = getTestRoleEntity();

        // when
        RoleEntity savedRoleEntity = roleRepository.saveAndFlush(testRoleEntity);

        // then
        entityManager.clear();
        assertNotNull(entityManager.find(RoleEntity.class, savedRoleEntity.getRoleId()));
    }

    @Test
    @Order(2)
    void saveToMerge() {
        // given
        RoleEntity testRoleEntity = saveTestRoleEntity();

        // when
        RoleEntity roleEntity = entityManager.find(RoleEntity.class, testRoleEntity.getRoleId());
        roleEntity.setName("changed role name");
        roleRepository.saveAndFlush(roleEntity);

        // then
        entityManager.clear();
        assertEquals(
                "changed role name",
                entityManager.find(RoleEntity.class, testRoleEntity.getRoleId()).getName()
        );
    }

    @Test
    @Order(2)
    void findById() {
        // given
        RoleEntity testRoleEntity = saveTestRoleEntity();

        // when
        RoleEntity roleEntity = roleRepository.findById(testRoleEntity.getRoleId()).orElseThrow();

        // then
        assertEquals(testRoleEntity.getRoleId(), roleEntity.getRoleId());
    }

    @Test
    @Order(3)
    void deleteById() {
        // given
        RoleEntity testRoleEntity = saveTestRoleEntity();

        // when
        roleRepository.deleteById(testRoleEntity.getRoleId());

        // then
        entityManager.flush();
        entityManager.clear();
        assertNull(entityManager.find(RoleEntity.class, testRoleEntity.getRoleId()));
    }
}