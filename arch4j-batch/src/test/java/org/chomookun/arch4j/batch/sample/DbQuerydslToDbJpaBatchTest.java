package org.chomookun.arch4j.batch.sample;

import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.batch.common.support.BatchTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ContextConfiguration(classes= DbQuerydslToDbJpaBatch.class)
public class DbQuerydslToDbJpaBatchTest extends BatchTestSupport {

    @Autowired
    @Qualifier("dbQuerydslToDbJpaJob")
    Job dbQuerydslToDbJpaJob;

    @Disabled
    @Test
    void dbQuerydslToDbJpaJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("size", 1234L)
                .toJobParameters();
        JobExecution jobExecution = getJobLauncherTestUtils(dbQuerydslToDbJpaJob).launchJob(jobParameters);
        assertEquals(BatchStatus.COMPLETED.name(), jobExecution.getStatus().name());
    }

}
