package org.chomookun.arch4j.batch.example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.batch.common.AbstractBatchConfigurer;
import org.chomookun.arch4j.batch.example.mapper.ExampleBackupMapper;
import org.chomookun.arch4j.batch.example.mapper.ExampleMapper;
import org.chomookun.arch4j.batch.example.tasklet.ClearExampleBackupTasklet;
import org.chomookun.arch4j.batch.example.vo.ExampleBackupVo;
import org.chomookun.arch4j.batch.example.vo.ExampleVo;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DbMybatisAndDbMybatisBatch extends AbstractBatchConfigurer {

    /**
     * DB(Mybatis) and DB(Mybatis) example job
     * @return job
     */
    @Bean
    Job dbMybatisAndDbMybatisJob() {
        return getJobBuilder("dbMybatisAndDbMybatisJob")
                .start(clearExampleBackupStep())
                .next(copyExampleToExampleBackupStep())
                .build();
    }

    /**
     * Clears example backup step
     * @return step
     */
    @Bean
    @JobScope
    Step clearExampleBackupStep() {
        Tasklet tasklet = ClearExampleBackupTasklet.builder()
                .jpaQueryFactory(getJpaQueryFactory())
                .build();
        return getStepBuilder("clearExampleBackupStep")
                .tasklet(tasklet, getTransactionManager())
                .build();
    }

    /**
     * Copy example to example backup step
     * @return step
     */
    @Bean
    @JobScope
    Step copyExampleToExampleBackupStep() {
        return getStepBuilder("copyExampleToExampleBackupStep")
                .<ExampleVo, ExampleBackupVo>chunk(1000, getTransactionManager())
                .reader(selectExampleReader(null))
                .processor(convertExampleToExampleBackupProcessor())
                .writer(insertExampleBackupWriter())
                .build();
    }

    /**
     * Selects example from table
     * @param type type
     * @return item reader
     */
    @Bean
    @StepScope
    MyBatisCursorItemReader<ExampleVo> selectExampleReader(@Value("#{jobParameters[type]}") String type) {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("type", type);
        return new MyBatisCursorItemReaderBuilder<ExampleVo>()
                .sqlSessionFactory(getSqlSessionFactory())
                .queryId(ExampleMapper.class.getName() + ".selectExamples")
                .parameterValues(parameterValues)
                .build();
    }

    /**
     * Converts example to example backup
     * @return item processor
     */
    @Bean
    @StepScope
    ItemProcessor<ExampleVo, ExampleBackupVo> convertExampleToExampleBackupProcessor() {
        return exampleVo -> {
            return ExampleBackupVo.builder()
                    .exampleId(exampleVo.getExampleId())
                    .name(exampleVo.getName())
                    .icon(exampleVo.getIcon())
                    .number(exampleVo.getNumber())
                    .decimalNumber(exampleVo.getDecimalNumber())
                    .dateTime(exampleVo.getDateTime())
                    .instantDateTime(exampleVo.getInstantDateTime())
                    .zonedDateTime(exampleVo.getZonedDateTime())
                    .date(exampleVo.getDate())
                    .time(exampleVo.getTime())
                    .enabled(exampleVo.getEnabled())
                    .type(exampleVo.getType())
                    .code(exampleVo.getCode())
                    .text(exampleVo.getText())
                    .cryptoText(exampleVo.getCryptoText())
                    .build();
        };
    }

    /**
     * Inserts example backup
     * @return item writer
     */
    @Bean
    @StepScope
    MyBatisBatchItemWriter<ExampleBackupVo> insertExampleBackupWriter() {
        return new MyBatisBatchItemWriterBuilder<ExampleBackupVo>()
                .sqlSessionFactory(getSqlSessionFactory())
                .statementId(ExampleBackupMapper.class.getName() + ".insertExampleBackup")
                .assertUpdates(false) // disable assert updates
                .build();
    }

}
