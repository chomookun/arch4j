package org.chomookun.arch4j.batch.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Getter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.chomookun.arch4j.batch.BatchProperties;
import org.chomookun.arch4j.batch.common.listener.JobListener;
import org.chomookun.arch4j.batch.common.listener.StepListener;
import org.springframework.aop.support.AopUtils;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.io.File;

@Configuration
public abstract class AbstractBatchConfigurer {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    @Getter
    private DataSource dataSource;

    @Autowired
    @Getter
    private PlatformTransactionManager transactionManager;

    @Autowired
    @Getter
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext
    @Getter
    private EntityManager entityManager;

    @Autowired
    @Getter
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    @Getter
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    @Getter
    private BatchProperties batchProperties;

    @Autowired
    @Getter
    private ConversionService conversionService;

    protected final JobBuilder getJobBuilder(String jobName) {
        return new JobBuilder(jobName, jobRepository)
                .listener(new JobListener());
    }

    protected final StepBuilder getStepBuilder(String stepName) {
        return new StepBuilder(stepName, jobRepository)
                .listener(new StepListener());
    }

    protected final FileSystemResource getFileSystemResource(String fileName) {
        Class<?> batchClass = AopUtils.getTargetClass(this);
        String batchClassName = batchClass.getName().replaceAll("\\$\\$.*", "");
        String filePath = batchProperties.getDataHome()
                + File.separator
                + batchClassName
                + File.separator
                + fileName;
        return new FileSystemResource(filePath);
    }

}
