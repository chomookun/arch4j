package org.chomookun.arch4j.core.execution.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.execution.dao.ExecutionEntity;
import org.chomookun.arch4j.core.execution.dao.ExecutionRepository;
import org.chomookun.arch4j.core.execution.model.Execution;
import org.chomookun.arch4j.core.execution.model.ExecutionSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExecutionService {

    private final ExecutionRepository executionRepository;

    private final PlatformTransactionManager transactionManager;

    public Execution start(String taskName) {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager, transactionDefinition);
        return transactionTemplate.execute(transactionStatus -> {
            ExecutionEntity executionEntity = ExecutionEntity.builder()
                    .executionId(IdGenerator.uuid())
                    .taskName(taskName)
                    .status(Execution.Status.RUNNING)
                    .startedAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();
            ExecutionEntity savedExecutionEntity = executionRepository.saveAndFlush(executionEntity);
            return Execution.from(savedExecutionEntity);
        });
    }

    public void update(Execution execution) {
        // last update time is within 10 seconds, skip
        if (execution.getUpdatedAt().isAfter(Instant.now().minusSeconds(3))) {
            return;
        } else {
            execution.setUpdatedAt(Instant.now());
        }
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager, transactionDefinition);
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            ExecutionEntity executionEntity = executionRepository.findById(execution.getExecutionId())
                    .orElseThrow();
            executionEntity.setUpdatedAt(execution.getUpdatedAt());
            executionEntity.setTotalCount(execution.getTotalCount().get());
            executionEntity.setSuccessCount(execution.getSuccessCount().get());
            executionEntity.setFailCount(execution.getFailCount().get());
            executionRepository.saveAndFlush(executionEntity);
        });
    }

    public void success(Execution execution) {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager, transactionDefinition);
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            ExecutionEntity executionEntity = executionRepository.findById(execution.getExecutionId()).orElseThrow();
            executionEntity.setStatus(Execution.Status.SUCCESS);
            executionEntity.setUpdatedAt(Instant.now());
            executionEntity.setEndedAt(Instant.now());
            executionRepository.saveAndFlush(executionEntity);
        });
    }

    public final void fail(Execution execution, Throwable e) {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager, transactionDefinition);
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            ExecutionEntity executionEntity = executionRepository.findById(execution.getExecutionId()).orElseThrow();
            executionEntity.setStatus(Execution.Status.FAILED);
            executionEntity.setUpdatedAt(Instant.now());
            executionEntity.setEndedAt(Instant.now());
            executionEntity.setMessage(ExceptionUtils.getStackTrace(e));
            executionRepository.saveAndFlush(executionEntity);
        });
    }

    /**
     * Gets executions
     * @param executionSearch execution search
     * @param pageable pageable
     * @return page of executions
     */
    public Page<Execution> getExecutions(ExecutionSearch executionSearch, Pageable pageable) {
        Page<ExecutionEntity> executionEntityPage = executionRepository.findAll(executionSearch, pageable);
        List<Execution> executions = executionEntityPage.getContent().stream()
                .map(Execution::from)
                .toList();
        return new PageImpl<>(executions, pageable, executionEntityPage.getTotalElements());
    }

}
