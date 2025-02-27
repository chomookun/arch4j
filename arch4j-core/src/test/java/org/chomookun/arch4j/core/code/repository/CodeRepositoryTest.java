package org.chomookun.arch4j.core.code.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.code.entity.CodeEntity;
import org.chomookun.arch4j.core.code.entity.CodeItemEntity;
import org.chomookun.arch4j.core.code.model.Code;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.code.model.CodeSearch;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@Slf4j
class CodeRepositoryTest extends CoreTestSupport {

    private final CodeRepository codeRepository;

    @Test
    void findAll() {
        // given
        CodeSearch codeSearch = CodeSearch.builder()
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        // when
        Page<CodeEntity> codeEntityPage = codeRepository.findAll(codeSearch, pageable);
        // then
        log.info("codeEntityPage: {}", codeEntityPage);
    }

    @Test
    void findAllWithUnPaged() {
        // given
        CodeSearch codeSearch = CodeSearch.builder()
                .build();
        Pageable pageable = Pageable.unpaged();
        // when
        Page<CodeEntity> codeEntityPage = codeRepository.findAll(codeSearch, pageable);
        // then
        log.info("codeEntityPage: {}", codeEntityPage);
    }

}