package org.chomookun.arch4j.batch.example;

import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.batch.common.test.BatchTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ContextConfiguration(classes= DbMybatisAndFileDsvBatch.class)
public class DbMybatisAndFileDsvBatchTest extends BatchTestSupport {

    @Autowired
    @Qualifier("dbMybatisAndFileDsvJob")
    Job dbMybatisAndFileDsvJob;

    @Test
    void dbMybatisToDbMybatisJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("_uuid", UUID.randomUUID().toString())
                .addString("exampleFileName", "example.dsv")
                .toJobParameters();
        JobExecution jobExecution = getJobLauncherTestUtils(dbMybatisAndFileDsvJob)
                .launchJob(jobParameters);
        assertEquals(BatchStatus.COMPLETED.name(), jobExecution.getStatus().name());
    }

}
