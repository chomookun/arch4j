package org.chomookun.arch4j.core.code;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.code.entity.CodeEntity;
import org.chomookun.arch4j.core.code.model.Code;
import org.chomookun.arch4j.core.code.model.CodeSearch;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class CodeServiceTest extends CoreTestSupport {

    final CodeService codeService;

    @Test
    void saveCodeForPersist() {
        // given
        Code code = Code.builder()
                .codeId("test")
                .name("test code")
                .build();
        // when
        Code savedCode = codeService.saveCode(code);
        // then
        entityManager.clear();
        assertNotNull(entityManager.find(CodeEntity.class, savedCode.getCodeId()));
    }

    @Test
    void saveCodeForMerge() {
        // given
        CodeEntity codeEntity = CodeEntity.builder()
                .codeId("test")
                .name("test code")
                .build();
        entityManager.persist(codeEntity);
        entityManager.flush();
        entityManager.clear();
        // when
        Code code = Code.from(codeEntity);
        code.setName("changed");
        Code savedCode = codeService.saveCode(code);
        // then
        entityManager.clear();
        assertEquals("changed", entityManager.find(CodeEntity.class, code.getCodeId()).getName());
    }

    @Test
    void getCode() {
        // given
        CodeEntity codeEntity = CodeEntity.builder()
                .codeId("test")
                .name("name")
                .build();
        entityManager.persist(codeEntity);
        entityManager.flush();
        entityManager.clear();
        // when
        Code code = codeService.getCode(codeEntity.getCodeId()).orElse(null);
        // then
        assertNotNull(code);
    }

    @Test
    void deleteCode() {
        // given
        CodeEntity codeEntity = CodeEntity.builder()
                .codeId("test")
                .name("name")
                .build();
        entityManager.persist(codeEntity);
        entityManager.flush();
        entityManager.clear();
        // when
        codeService.deleteCode(codeEntity.getCodeId());
        // then
        assertNull(entityManager.find(CodeEntity.class, codeEntity.getCodeId()));
    }

    @Test
    void getCodes() {
        // given
        CodeEntity codeEntity = CodeEntity.builder()
                .codeId("test")
                .name("name")
                .build();
        entityManager.persist(codeEntity);
        entityManager.flush();
        entityManager.clear();
        // when
        CodeSearch codeSearch = CodeSearch.builder()
                .name(codeEntity.getName())
                .build();
        Page<Code> codePage = codeService.getCodes(codeSearch, PageRequest.of(0,10));
        // then
        assertTrue(codePage.getContent().stream()
                .anyMatch(e -> e.getName().contains(codeSearch.getName())));
    }

}