package org.chomookun.arch4j.batch.example.tasklet;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.chomookun.arch4j.batch.common.support.ManualTransactionHandler;
import org.chomookun.arch4j.core.example.entity.QExampleBackupEntity;
import org.chomookun.arch4j.core.example.entity.QExampleBackupItemEntity;
import org.chomookun.arch4j.core.sample.entity.QSampleBackupEntity;
import org.chomookun.arch4j.core.sample.entity.QSampleEntity;
import org.chomookun.arch4j.core.sample.entity.SampleEntity;
import org.chomookun.arch4j.core.sample.model.SampleType;
import org.chomookun.arch4j.core.sample.repository.SampleRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Random;

@Slf4j
@Builder
public class ClearExampleBackupTasklet implements Tasklet {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        // clears example backup
        jpaQueryFactory.delete(QExampleBackupEntity.exampleBackupEntity).execute();
        // clears example backup item
        jpaQueryFactory.delete(QExampleBackupItemEntity.exampleBackupItemEntity).execute();
        // returns
        return RepeatStatus.FINISHED;
    }

}
