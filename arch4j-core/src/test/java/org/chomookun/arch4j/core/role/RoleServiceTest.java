package org.chomookun.arch4j.core.role;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.security.RoleService;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.security.model.Role;
import org.chomookun.arch4j.core.security.model.RoleSearch;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.chomookun.arch4j.core.security.entity.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class RoleServiceTest extends CoreTestSupport {

    private final RoleService roleService;

    @Test
    void saveRoleForPersist() {
        // given
        Role role = Role.builder()
                .roleId("test")
                .name("test role")
                .build();
        // when
        Role savedRole = roleService.saveRole(role);
        // then
        entityManager.clear();
        assertNotNull(entityManager.find(RoleEntity.class, savedRole.getRoleId()));
    }

    @Test
    void saveRoleForMerge() {
        // given
        RoleEntity roleEntity = RoleEntity.builder()
                .roleId(IdGenerator.uuid())
                .name("test role")
                .build();
        entityManager.persist(roleEntity);
        entityManager.flush();
        entityManager.clear();
        // when;
        Role role = Role.from(entityManager.find(RoleEntity.class, roleEntity.getRoleId()));
        role.setName("changed");
        Role savedRole = roleService.saveRole(role);
        // then
        entityManager.clear();
        assertEquals("changed", entityManager.find(RoleEntity.class, role.getRoleId()).getName());
    }

    @Test
    void getRole() {
        // given
        RoleEntity roleEntity = RoleEntity.builder()
                .roleId("test")
                .name("name")
                .build();
        entityManager.persist(roleEntity);
        entityManager.flush();
        entityManager.clear();
        // when
        Role role = roleService.getRole(roleEntity.getRoleId()).orElseThrow();
        // then
        assertNotNull(role);
    }

    @Test
    void deleteRole() {
        // given
        RoleEntity roleEntity = RoleEntity.builder()
                .roleId("test")
                .name("name")
                .build();
        entityManager.persist(roleEntity);
        entityManager.flush();
        entityManager.clear();
        // when
        roleService.deleteRole(roleEntity.getRoleId());
        // then
        assertNull(entityManager.find(RoleEntity.class, roleEntity.getRoleId()));
    }

    @Test
    void getRoles() {
        // given
        RoleEntity roleEntity = RoleEntity.builder()
                .roleId("test")
                .name("name")
                .build();
        entityManager.persist(roleEntity);
        entityManager.flush();
        entityManager.clear();
        RoleSearch roleSearch = RoleSearch.builder()
                .roleId(roleEntity.getRoleId())
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        // when
        Page<Role> rolePage = roleService.getRoles(roleSearch, pageable);
        // then
        assertTrue(rolePage.getContent().stream().anyMatch(e -> e.getRoleId().contains(roleSearch.getRoleId())));
    }

}