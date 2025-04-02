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
@ContextConfiguration(classes= DbMybatisAndDbMybatisBatch.class)
public class DbMybatisAndDbMybatisBatchTest extends BatchTestSupport {

    @Autowired
    @Qualifier("dbMybatisAndDbMybatisJob")
    Job dbMybatisAndDbMybatisJob;

    @Test
    void dbMybatisToDbMybatisJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("_uuid", UUID.randomUUID().toString())
                .toJobParameters();
        JobExecution jobExecution = getJobLauncherTestUtils(dbMybatisAndDbMybatisJob)
                .launchJob(jobParameters);
        assertEquals(BatchStatus.COMPLETED.name(), jobExecution.getStatus().name());
    }

}
