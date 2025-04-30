package org.chomookun.arch4j.core.role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.security.AuthorityService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.security.entity.AuthorityEntity;
import org.chomookun.arch4j.core.security.model.Authority;
import org.chomookun.arch4j.core.security.model.AuthoritySearch;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@Slf4j
class AuthorityServiceTest extends CoreTestSupport {

    private final AuthorityService authorityService;

    @Test
    void saveAuthorityForPersist() {
        // given
        Authority newAuthority = Authority.builder()
                .authorityId(IdGenerator.uuid())
                .name("test authority")
                .build();
        // when
        authorityService.saveAuthority(newAuthority);
        // then
        entityManager.clear();
        AuthorityEntity authorityEntity = entityManager.find(AuthorityEntity.class, newAuthority.getAuthorityId());
        assertNotNull(authorityEntity);
    }

    @Test
    void saveAuthorityForMerge() {
        // given
        AuthorityEntity authorityEntity = AuthorityEntity.builder()
                .authorityId(IdGenerator.uuid())
                .name("test authority")
                .build();
        entityManager.persist(authorityEntity);
        entityManager.flush();
        entityManager.clear();
        // when
        Authority authority = Authority.from(authorityEntity);
        authority.setName("changed");
        authorityService.saveAuthority(authority);
        // then
        entityManager.clear();
        assertEquals("changed", entityManager.find(AuthorityEntity.class, authority.getAuthorityId()).getName());
    }

    @Test
    @Order(3)
    void getAuthority() {
        // given
        AuthorityEntity authorityEntity = AuthorityEntity.builder()
                .authorityId(IdGenerator.uuid())
                .name("test authority")
                .build();
        entityManager.persist(authorityEntity);
        entityManager.flush();
        entityManager.clear();
        // when
        Authority authority = authorityService.getAuthority(authorityEntity.getAuthorityId()).orElseThrow();
        // then
        log.info("authority: {}", authority);
        assertEquals(authorityEntity.getAuthorityId(), authority.getAuthorityId());
    }

    @Test
    void deleteAuthority() {
        // given
        AuthorityEntity authorityEntity = AuthorityEntity.builder()
                .authorityId("test")
                .name("test authority")
                .build();
        entityManager.persist(authorityEntity);
        entityManager.flush();
        entityManager.clear();
        // when
        authorityService.deleteAuthority(authorityEntity.getAuthorityId());
        // then
        assertNull(entityManager.find(AuthorityEntity.class, authorityEntity.getAuthorityId()));
    }

    @Test
    void getAuthorities() {
        // given
        AuthorityEntity authorityEntity = AuthorityEntity.builder()
                .authorityId("test")
                .name("test authority")
                .build();
        entityManager.persist(authorityEntity);
        entityManager.flush();
        entityManager.clear();
        AuthoritySearch authoritySearch = AuthoritySearch.builder()
                .authorityId(authorityEntity.getAuthorityId())
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        // when
        Page<Authority> rolePage = authorityService.getAuthorities(authoritySearch, pageable);
        // then
        assertTrue(rolePage.getContent().stream().allMatch(e -> e.getAuthorityId().contains(authoritySearch.getAuthorityId())));
    }

}