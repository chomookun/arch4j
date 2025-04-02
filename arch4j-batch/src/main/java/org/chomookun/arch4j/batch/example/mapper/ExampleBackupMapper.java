package org.chomookun.arch4j.batch.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.chomookun.arch4j.batch.example.vo.ExampleBackupVo;

/**
 * Example Backup Mapper
 */
@Mapper
public interface ExampleBackupMapper {

    /**
     * Inserts example backup
     * @param exampleBackupVo example backup vo
     * @return number of rows affected
     */
    int insertExampleBackup(ExampleBackupVo exampleBackupVo);

}
