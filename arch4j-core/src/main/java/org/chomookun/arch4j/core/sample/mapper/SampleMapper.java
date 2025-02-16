package org.chomookun.arch4j.core.sample.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
import org.chomookun.arch4j.core.sample.vo.SampleVo;
import org.chomookun.arch4j.core.sample.model.SampleSearch;

import java.util.List;

@Mapper
public interface SampleMapper {

    List<SampleVo> selectSamples(SampleSearch sampleSearch, RowBounds rowBounds);

    Long selectSamplesCount(SampleSearch sampleSearch);

}
