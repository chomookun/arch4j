package org.chomookun.arch4j.core.example.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.chomookun.arch4j.core.example.entity.ExampleEntity;
import org.chomookun.arch4j.core.example.model.ExampleSearch;
import org.chomookun.arch4j.core.example.vo.ExampleItemVo;
import org.chomookun.arch4j.core.example.vo.ExampleVo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@Slf4j
class ExampleMapperTest extends CoreTestSupport {

    private final ExampleMapper exampleMapper;

    @Test
    void selectExamples() {
        // when
        ExampleSearch exampleSearch = ExampleSearch.builder()
                .exampleId("%a%")
                .build();
        RowBounds rowBounds = new RowBounds(0, 10);
        List<ExampleVo> exampleVos = exampleMapper.selectExamples(exampleSearch, rowBounds);
        // then
        log.info("exampleVos: {}", exampleVos);
    }

    @Test
    void selectExamplesCount() {
        // when
        ExampleSearch exampleSearch = ExampleSearch.builder()
                .exampleId("%a%")
                .build();
        long total = exampleMapper.selectExamplesCount(exampleSearch);
        // then
        log.info("total: {}", total);
    }

    @Test
    void insertExample() {
        // when
        ExampleVo exampleVo = ExampleVo.builder()
                .exampleId(IdGenerator.uuid())
                .name("aaa")
                .enabled(true)
                .cryptoText("crypto text")
                .build();
        int result = exampleMapper.insertExample(exampleVo);
        // then
        log.info("result: {}", result);
        ExampleEntity exampleEntity = entityManager.find(ExampleEntity.class, exampleVo.getExampleId());
        log.info("exampleEntity: {}", exampleEntity);
    }

    @Test
    void selectExampleItems() {
        // when
        String exampleId = "aaa";
        List<ExampleItemVo> exampleItemVos = exampleMapper.selectExampleItems(exampleId);
        // then
        log.info("exampleItemVos: {}", exampleItemVos);
    }

}