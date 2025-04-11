package org.chomookun.arch4j.core.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.chomookun.arch4j.core.example.model.ExampleSearch;
import org.chomookun.arch4j.core.example.vo.ExampleItemVo;
import org.chomookun.arch4j.core.example.vo.ExampleVo;

import java.util.List;

@Mapper
public interface ExampleMapper {

    List<ExampleVo> selectExamples(ExampleSearch exampleSearch, RowBounds rowBounds);

    long selectExamplesCount(ExampleSearch exampleSearch);

    ExampleVo selectExample(@Param("exampleId") String exampleId);

    int insertExample(ExampleVo exampleVo);

    int updateExample(ExampleVo exampleVo);

    int deleteExample(@Param("exampleId") String exampleId);

    List<ExampleItemVo> selectExampleItems(@Param("exampleId") String exampleId);

    int insertExampleItem(ExampleItemVo exampleItemVo);

    int deleteExampleItems(@Param("exampleId") String exampleId);

}
