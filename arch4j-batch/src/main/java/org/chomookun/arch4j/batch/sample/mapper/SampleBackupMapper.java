package org.chomookun.arch4j.batch.sample.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.chomookun.arch4j.batch.sample.vo.SampleBackupVo;

@Mapper
public interface SampleBackupMapper {

    Integer insertSampleBackup(SampleBackupVo sampleBackupVo);

}
