package org.oopscraft.arch4j.batch.sample.tasklet;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.oopscraft.arch4j.batch.support.ManualTransactionHandler;
import org.oopscraft.arch4j.core.sample.dao.QSampleBackupEntity;
import org.oopscraft.arch4j.core.sample.dao.QSampleEntity;
import org.oopscraft.arch4j.core.sample.dao.SampleEntity;
import org.oopscraft.arch4j.core.sample.dao.SampleRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Builder
public class CreateSampleDbTasklet implements Tasklet {

    private Integer size;

    @Builder.Default
    private String lang = "en";

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    @Autowired
    SampleRepository sampleRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

        // clear sample
        clearSampleDb();

        // clear sampleBackup
        clearSampleBackupDb();

        // create sample
        createSampleDb();

        // returns
        return RepeatStatus.FINISHED;
    }

    private void clearSampleDb() {
        try (ManualTransactionHandler manualTransactionHandler = new ManualTransactionHandler(transactionManager)) {
            jpaQueryFactory.delete(QSampleEntity.sampleEntity).execute();
            manualTransactionHandler.commit();
        }
    }

    private void clearSampleBackupDb() {
        try (ManualTransactionHandler manualTransactionHandler = new ManualTransactionHandler(transactionManager)) {
            jpaQueryFactory.delete(QSampleBackupEntity.sampleBackupEntity).execute();
            manualTransactionHandler.commit();
        }
    }

    private void createSampleDb() {
        try (ManualTransactionHandler manualTransactionUtil = new ManualTransactionHandler(transactionManager)) {
            for (int i = 0; i < size; i++) {
                Faker faker = new Faker(new Locale(lang), new Random(i));
                SampleEntity sample = SampleEntity.builder()
                        .sampleId(UUID.randomUUID().toString())
                        .name(faker.name().lastName() + faker.name().firstName())
                        .number(faker.number().numberBetween(-100, 100))
                        .longNumber(faker.number().numberBetween(Long.MIN_VALUE, Long.MAX_VALUE))
                        .doubleNumber(faker.number().randomDouble(4, -100000, 100000))
                        .bigDecimal(BigDecimal.valueOf(faker.number().randomDouble(4, -1234567890, 1234567890)))
                        .sqlDate(new java.sql.Date(System.currentTimeMillis() + faker.number().numberBetween(0, 1234)))
                        .utilDate(new java.util.Date(System.currentTimeMillis() + faker.number().numberBetween(0, 1234)))
                        .timestamp(new java.sql.Timestamp(System.currentTimeMillis() + faker.number().numberBetween(0, 1234)))
                        .localDate(LocalDate.now().plusDays(faker.number().numberBetween(0, 123)))
                        .localDateTime(LocalDateTime.now().withNano(0).plusSeconds(faker.number().numberBetween(0, 1234)))
                        .lobText(faker.address().fullAddress())
                        .cryptoText(faker.business().creditCardNumber())
                        .build();
                sampleRepository.saveAndFlush(sample);

                // interval commit
                if (i % 100 == 0) {
                    manualTransactionUtil.commit();
                }
            }

            // final flush commit
            manualTransactionUtil.commit();
        }

        // check create count
        long totalCount = sampleRepository.count();
        if(totalCount != this.size) {
            throw new RuntimeException(String.format("Create SampleEntity count mismatch - size:%d, totalCount:%d", size, totalCount));
        }
    }

}
