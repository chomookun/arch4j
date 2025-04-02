package org.chomookun.arch4j.batch.sample;

import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.batch.common.test.BatchTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ContextConfiguration(classes= FileFldToFileDsvBatch.class)
public class FileFldToFileDsvBatchTest extends BatchTestSupport {

    @Autowired
    @Qualifier("fileFldToFileDsvJob")
    Job fileFldToFileDsvJob;

//    @Disabled
//    @Test
//    void fileFldToFileDsvJob() throws Exception {
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addLong("size", 1234L)
//                .addString("lang", "ko")
//                .addString("inputFile", getDataDirectory(this.getClass()) + "fileFldToFileDsv.input.dsv")
//                .addString("outputFile", getDataDirectory(this.getClass()) + "fileFldToFileDsv.output.fld")
//                .toJobParameters();
//        JobExecution jobExecution = getJobLauncherTestUtils(fileFldToFileDsvJob).launchJob(jobParameters);
//        assertEquals(BatchStatus.COMPLETED.name(), jobExecution.getStatus().name());
//    }
//
//    @Disabled
//    @Test
//    void fileFldToFileDsvJobWithEncoding() throws Exception {
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addLong("size", 1234L)
//                .addString("lang", "ko")
//                .addString("inputFile", getDataDirectory(this.getClass()) + "fileFldToFileDsvWithEncoding.input.dsv")
//                .addString("inputEncoding", "euc-kr")
//                .addString("outputFile", getDataDirectory(this.getClass()) + "fileFldToFileDsvWithEncoding.output.fld")
//                .addString("outputEncoding", "euc-kr")
//                .toJobParameters();
//        JobExecution jobExecution = getJobLauncherTestUtils(fileFldToFileDsvJob).launchJob(jobParameters);
//        assertEquals(BatchStatus.COMPLETED.name(), jobExecution.getStatus().name());
//    }
//
//    @Disabled
//    @Test
//    void fileFldToFileDsvJobWithLineSeparator() throws Exception {
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addLong("size", 1234L)
//                .addString("lang", "ko")
//                .addString("inputFile", getDataDirectory(this.getClass()) + "fileFldToFileDsvWithLineSeparator.input.dsv")
//                .addString("inputLineSeparator", "\r\n")
//                .addString("outputFile", getDataDirectory(this.getClass()) + "fileFldToFileDsvWithLineSeparator.input.fld")
//                .addString("outputLineSeparator", "\r\n")
//                .toJobParameters();
//        JobExecution jobExecution = getJobLauncherTestUtils(fileFldToFileDsvJob).launchJob(jobParameters);
//        assertEquals(BatchStatus.COMPLETED.name(), jobExecution.getStatus().name());
//    }
//
//    @Disabled
//    @Test
//    void fileFldToFileDsvJobWithDelimiter() throws Exception {
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addLong("size", 1234L)
//                .addString("lang", "ko")
//                .addString("inputFile", getDataDirectory(this.getClass()) + "fileFldToFileDsvWithDelimiter.input.dsv")
//                .addString("inputDelimiter", "^")
//                .addString("outputFile", getDataDirectory(this.getClass()) + "fileFldToFileDsvWithDelimiter.input.fld")
//                .toJobParameters();
//        JobExecution jobExecution = getJobLauncherTestUtils(fileFldToFileDsvJob).launchJob(jobParameters);
//        assertEquals(BatchStatus.COMPLETED.name(), jobExecution.getStatus().name());
//    }

}
