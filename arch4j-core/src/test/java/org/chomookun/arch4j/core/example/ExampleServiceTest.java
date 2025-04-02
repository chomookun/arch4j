package org.chomookun.arch4j.core.example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.chomookun.arch4j.core.example.entity.ExampleEntity;
import org.chomookun.arch4j.core.example.model.Example;
import org.chomookun.arch4j.core.example.model.ExampleSearch;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@Slf4j
class ExampleServiceTest extends CoreTestSupport {

    private final ExampleService exampleService;

    @Test
    void saveExample() {
        // given
        Example example = Example.builder()
                .name("test test")
                .build();
        // when
        Example savedExample = exampleService.saveExample(example);
        // then
        log.info("savedExample: {}", savedExample);
    }

    @Test
    void getExample() {
        // given
        ExampleEntity exampleEntity = ExampleEntity.builder()
                .exampleId(IdGenerator.uuid())
                .text("test text")
                .build();
        entityManager.persist(exampleEntity);
        entityManager.flush();
        // when
        Example example = exampleService.getExample(exampleEntity.getExampleId()).orElseThrow();
        // then
        log.info("example: {}", example);
    }

    @Test
    void deleteExample() {
        // given
        ExampleEntity exampleEntity = ExampleEntity.builder()
                .exampleId(IdGenerator.uuid())
                .build();
        entityManager.persist(exampleEntity);
        entityManager.flush();
        // when
        exampleService.deleteExample(exampleEntity.getExampleId());
        // then
        assertNull(entityManager.find(ExampleEntity.class, exampleEntity.getExampleId()));
    }

    @Test
    void getExamples() {
        // given
        ExampleEntity exampleEntity = ExampleEntity.builder()
                .exampleId(IdGenerator.uuid())
                .build();
        entityManager.persist(exampleEntity);
        entityManager.flush();
        // when
        ExampleSearch exampleSearch = ExampleSearch.builder()
                .exampleId(exampleEntity.getExampleId())
                .build();
        Pageable pageable = Pageable.unpaged();
        Page<Example> examplePage = exampleService.getExamples(exampleSearch, Pageable.unpaged());
        // then
        log.info("examplePage: {}", examplePage);
    }
}