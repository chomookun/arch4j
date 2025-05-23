package org.chomookun.arch4j.batch.common.test;

import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.batch.BatchConfiguration;
import org.chomookun.arch4j.batch.BatchProperties;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.io.File;

@Slf4j
@SpringBootTest(classes = BatchConfiguration.class)
@SpringBatchTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class BatchTestSupport {

    @Autowired
    private BatchProperties batchProperties;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobLauncher jobLauncher;

    protected final JobLauncherTestUtils getJobLauncherTestUtils(Job job) {
        JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJobRepository(jobRepository);
        jobLauncherTestUtils.setJobLauncher(jobLauncher);
        jobLauncherTestUtils.setJob(job);
        return jobLauncherTestUtils;
    }

}
