package org.chomookun.arch4j.batch.example.tasklet;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.example.entity.QExampleBackupEntity;
import org.chomookun.arch4j.core.example.entity.QExampleBackupItemEntity;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

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
