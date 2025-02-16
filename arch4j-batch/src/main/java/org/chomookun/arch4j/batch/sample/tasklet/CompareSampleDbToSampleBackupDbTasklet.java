package org.chomookun.arch4j.batch.sample.tasklet;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.chomookun.arch4j.core.sample.entity.QSampleEntity;
import org.chomookun.arch4j.core.sample.entity.SampleBackupEntity;
import org.chomookun.arch4j.core.sample.entity.SampleEntity;
import org.chomookun.arch4j.core.sample.repository.SampleBackupRepository;
import org.chomookun.arch4j.core.sample.repository.SampleRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.time.ZoneOffset;
import java.util.Optional;

@Slf4j
@Builder
public class CompareSampleDbToSampleBackupDbTasklet implements Tasklet {

    @Autowired
    SampleRepository sampleRepository;

    @Autowired
    SampleBackupRepository sampleBackupRepository;

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        // compare count
        long sampleCount = sampleRepository.count();
        long sampleBackupCount = sampleBackupRepository.count();
        log.info("== sampleCount: {}", sampleCount);
        log.info("== sampleBackupCount: {}", sampleBackupCount);
        Assert.isTrue(sampleCount == sampleBackupCount, String.format("sampleCount[%d], sampleBackupCount[%d] is mismatched.", sampleCount, sampleBackupCount));

        // compare column value
        jpaQueryFactory.selectFrom(QSampleEntity.sampleEntity).stream().forEach(item ->{
            SampleEntity sampleEntity = sampleRepository.findById(item.getSampleId()).orElseThrow();
            SampleBackupEntity sampleBackupEntity = sampleBackupRepository.findById(item.getSampleId()).orElseThrow();
            int result = new CompareToBuilder()
                    .append(sampleEntity.getSampleId(), sampleBackupEntity.getSampleId())
                    .append(sampleEntity.getSampleName(), sampleBackupEntity.getSampleName())
                    .append(sampleEntity.getSampleType(), sampleBackupEntity.getSampleType())
                    .append(sampleEntity.getNumber(), sampleBackupEntity.getNumber())
                    .append(sampleEntity.getBigDecimal(), sampleBackupEntity.getBigDecimal())
                    .append(sampleEntity.getLongNumber(), sampleBackupEntity.getLongNumber())
                    .append(sampleEntity.getDoubleNumber(), sampleBackupEntity.getDoubleNumber())
                    .append(Optional.ofNullable(sampleEntity.getLocalDateTime()).map(v->v.toEpochSecond(ZoneOffset.UTC)).orElse(null),
                            Optional.ofNullable(sampleBackupEntity.getLocalDateTime()).map(v->v.toEpochSecond(ZoneOffset.UTC)).orElse(null))
                    .append(sampleEntity.getLocalDate(), sampleBackupEntity.getLocalDate())
                    .append(sampleEntity.getLocalTime(), sampleBackupEntity.getLocalTime())
                    .append(sampleEntity.getLobText(), sampleBackupEntity.getLobText())
                    .append(sampleEntity.getCryptoText(), sampleBackupEntity.getCryptoText())
                    .toComparison();
            if(result != 0) {
                log.warn("{}", sampleEntity);
                log.warn("{}", sampleBackupEntity);
                throw new RuntimeException("data mismatch");
            }
        });
        log.info("== context is same");

        // return
        return RepeatStatus.FINISHED;
    }
}
