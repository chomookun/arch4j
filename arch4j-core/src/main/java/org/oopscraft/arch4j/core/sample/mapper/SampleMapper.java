package org.oopscraft.arch4j.core.sample.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
import org.oopscraft.arch4j.core.sample.SampleSearch;

import java.util.List;

@Mapper
public interface SampleMapper {

    public List<SampleVo> selectSamples(SampleSearch sampleSearch, RowBounds rowBounds);

    public Long selectSamplesCount(SampleSearch sampleSearch);

}
