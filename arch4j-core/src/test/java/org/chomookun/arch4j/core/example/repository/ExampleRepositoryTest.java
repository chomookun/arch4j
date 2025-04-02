package org.chomookun.arch4j.core.example.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.chomookun.arch4j.core.example.entity.ExampleEntity;
import org.chomookun.arch4j.core.example.model.ExampleSearch;
import org.chomookun.arch4j.core.role.entity.AuthorityEntity;
import org.chomookun.arch4j.core.role.model.AuthoritySearch;
import org.chomookun.arch4j.core.role.repository.AuthorityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@Slf4j
class ExampleRepositoryTest extends CoreTestSupport {

    private final ExampleRepository exampleRepository;

    @Test
    void findAll() {
        // given
        ExampleEntity testExampleEntity = ExampleEntity.builder()
                .exampleId("test")
                .text("test")
                .build();
        entityManager.persist(testExampleEntity);
        entityManager.flush();
        // when
        ExampleSearch exampleSearch = ExampleSearch.builder()
                .exampleId(testExampleEntity.getExampleId())
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<ExampleEntity> exampleEntityPage = exampleRepository.findAll(exampleSearch, pageable);
        // then
        log.info("exampleEntityPage: {}", exampleEntityPage);
        assertFalse(exampleEntityPage.getContent().isEmpty());
        assertTrue(exampleEntityPage.getContent().stream()
                .allMatch(exampleEntity ->
                        exampleEntity.getExampleId().contains(testExampleEntity.getExampleId())));
    }

}