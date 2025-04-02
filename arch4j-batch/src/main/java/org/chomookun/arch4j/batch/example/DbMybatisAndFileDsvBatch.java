package org.chomookun.arch4j.batch.example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.batch.common.AbstractBatchConfigurer;
import org.chomookun.arch4j.batch.example.dto.ExampleFile;
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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.io.FileSystemResource;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DbMybatisAndFileDsvBatch extends AbstractBatchConfigurer {

    /**
     * Job: dbMybatisAndFileDsvJob
     * @return job
     */
    @Bean
    Job dbMybatisAndFileDsvJob() {
        return getJobBuilder("dbMybatisAndFileDsvJob")
                .start(clearExampleBackupStep())
                .next(exportExampleToExampleFileStep())
                .next(importExampleFileToExampleBackupStep())
                .build();
    }

    /**
     * Clears example backup data
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
     * Exports example data to example file
     * @return step
     */
    @Bean
    @JobScope
    Step exportExampleToExampleFileStep() {
        return getStepBuilder("exportExampleToExampleFileStep")
                .<ExampleVo, ExampleFile>chunk(10, getTransactionManager())
                .reader(exampleReader(null))
                .processor(exampleToExampleFileProcessor())
                .writer(exampleFileWriter(null))
                .build();
    }

    /**
     * Imports example file to example backup table
     * @return step
     */
    @Bean
    @JobScope
    Step importExampleFileToExampleBackupStep() {
        return getStepBuilder("importExampleFileToExampleBackupStep")
                .<ExampleFile, ExampleBackupVo>chunk(1, getTransactionManager())
                .reader(exampleFileReader(null))
                .processor(exampleFileToExampleBackupProcessor())
                .writer(exampleBackupWriter())
                .build();
    }

    /**
     * Selects example from table
     * @param type type
     * @return item reader
     */
    @Bean
    @StepScope
    MyBatisCursorItemReader<ExampleVo> exampleReader(@Value("#{jobParameters[type]}") String type) {
        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("type", type);
        return new MyBatisCursorItemReaderBuilder<ExampleVo>()
                .sqlSessionFactory(getSqlSessionFactory())
                .queryId(ExampleMapper.class.getName() + ".selectExamples")
                .parameterValues(parameterValues)
                .build();
    }

    /**
     * Creates example to example file processor
     * @return processor
     */
    @Bean
    @StepScope
    ItemProcessor<ExampleVo, ExampleFile> exampleToExampleFileProcessor() {
        ConversionService conversionService = getConversionService();
        return exampleVo -> {
            return ExampleFile.builder()
                    .exampleId(exampleVo.getExampleId())
                    .name(exampleVo.getName())
                    .icon(exampleVo.getIcon())
                    .number(conversionService.convert(exampleVo.getNumber(), String.class))
                    .decimalNumber(conversionService.convert(exampleVo.getDecimalNumber(), String.class))
                    .dateTime(conversionService.convert(exampleVo.getDateTime(), String.class))
                    .instantDateTime(conversionService.convert(exampleVo.getInstantDateTime(), String.class))
                    .zonedDateTime(conversionService.convert(exampleVo.getZonedDateTime(), String.class))
                    .date(conversionService.convert(exampleVo.getDate(), String.class))
                    .time(conversionService.convert(exampleVo.getTime(), String.class))
                    .enabled(exampleVo.getEnabled())
                    .type(exampleVo.getType())
                    .code(exampleVo.getCode())
                    .text(exampleVo.getText())
                    .cryptoText(exampleVo.getCryptoText())
                    .build();
        };
    }

    /**
     * Writes example to file
     * @param exampleFileName example file name
     * @return item writer
     */
    @Bean
    @StepScope
    FlatFileItemWriter<ExampleFile> exampleFileWriter(@Value("#{jobParameters[exampleFileName]}") String exampleFileName) {
        // resource
        FileSystemResource resource = getFileSystemResource(exampleFileName);
        // line aggregator
        DelimitedLineAggregator<ExampleFile> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter("\t");
        BeanWrapperFieldExtractor<ExampleFile> fieldExtractor = new BeanWrapperFieldExtractor<>();
        String[] fieldNames = Arrays.stream(ExampleFile.class.getDeclaredFields())
                .map(Field::getName)
                .toArray(String[]::new);
        fieldExtractor.setNames(fieldNames);
        lineAggregator.setFieldExtractor(fieldExtractor);
        // file item writer
        FlatFileItemWriter<ExampleFile> flatFileItemWriter = new FlatFileItemWriter<>();
        flatFileItemWriter.setResource(resource);
        flatFileItemWriter.setLineAggregator(lineAggregator);
        flatFileItemWriter.setEncoding("UTF-8");
        return flatFileItemWriter;
    }

    /**
     * Creates example file reader
     * @param exampleFileName example file name
     * @return file item reader
     */
    @Bean
    @StepScope
    FlatFileItemReader<ExampleFile> exampleFileReader(@Value("#{jobParameters[exampleFileName]}") String exampleFileName) {
        // resource
        FileSystemResource resource = getFileSystemResource(exampleFileName);
        // line tokenizer
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("\t");
        String[] fieldNames = Arrays.stream(ExampleFile.class.getDeclaredFields())
                .map(Field::getName)
                .toArray(String[]::new);
        lineTokenizer.setNames(fieldNames);
        // field set mapper
        BeanWrapperFieldSetMapper<ExampleFile> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(ExampleFile.class);
        // line mapper
        DefaultLineMapper<ExampleFile> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        // file item reader
        FlatFileItemReader<ExampleFile> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(resource);
        flatFileItemReader.setLineMapper(lineMapper);
        flatFileItemReader.setEncoding("UTF-8");
        return flatFileItemReader;
    }

    /**
     * Creates processor to convert example file to example backup
     * @return processor
     */
    @Bean
    @StepScope
    ItemProcessor<ExampleFile, ExampleBackupVo> exampleFileToExampleBackupProcessor() {
        ConversionService conversionService = getConversionService();
        return exampleDsvFile -> {
            return ExampleBackupVo.builder()
                    .exampleId(exampleDsvFile.getExampleId())
                    .name(exampleDsvFile.getName())
                    .icon(exampleDsvFile.getIcon())
                    .number(conversionService.convert(exampleDsvFile.getNumber(), Integer.class))
                    .decimalNumber(conversionService.convert(exampleDsvFile.getDecimalNumber(), BigDecimal.class))
                    .dateTime(conversionService.convert(exampleDsvFile.getDateTime(), LocalDateTime.class))
                    .instantDateTime(conversionService.convert(exampleDsvFile.getInstantDateTime(), Instant.class))
                    .zonedDateTime(conversionService.convert(exampleDsvFile.getZonedDateTime(), ZonedDateTime.class))
                    .date(conversionService.convert(exampleDsvFile.getDate(), LocalDate.class))
                    .time(conversionService.convert(exampleDsvFile.getTime(), LocalTime.class))
                    .enabled(exampleDsvFile.getEnabled())
                    .type(exampleDsvFile.getType())
                    .code(exampleDsvFile.getCode())
                    .text(exampleDsvFile.getText())
                    .cryptoText(exampleDsvFile.getCryptoText())
                    .build();
        };
    }

    /**
     * Creates example backup writer
     * @return item writer
     */
    @Bean
    @StepScope
    MyBatisBatchItemWriter<ExampleBackupVo> exampleBackupWriter() {
        return new MyBatisBatchItemWriterBuilder<ExampleBackupVo>()
                .sqlSessionFactory(getSqlSessionFactory())
                .statementId(ExampleBackupMapper.class.getName() + ".insertExampleBackup")
                .assertUpdates(false) // disable assert updates
                .build();
    }

}
