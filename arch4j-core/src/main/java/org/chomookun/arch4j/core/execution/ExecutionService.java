package org.chomookun.arch4j.core.execution;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.execution.entity.ExecutionEntity;
import org.chomookun.arch4j.core.execution.repository.ExecutionRepository;
import org.chomookun.arch4j.core.execution.model.Execution;
import org.chomookun.arch4j.core.execution.model.ExecutionSearch;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExecutionService implements ApplicationListener<ContextClosedEvent> {

    private final ExecutionRepository executionRepository;

    private final PlatformTransactionManager transactionManager;

    private final List<Execution> runningExecutions = Collections.synchronizedList(new ArrayList<>());

    private final AtomicBoolean shuttingDown = new AtomicBoolean(false);

    /**
     * Marks all running executions as STOPPED when application is shutting down.
     * @param event context closed event
     */
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        shuttingDown.set(true);
        for (int i = runningExecutions.size() -1 ; i >= 0; i --) {
            Execution execution = runningExecutions.get(i);
            stop(execution);
        }
        log.info("ExecutionService - All running executions marked as STOPPED.");
    }

    /**
     * Starts a new task execution
     * @param taskName task name
     * @return execution
     */
    public Execution start(String taskName) {
        if (shuttingDown.get()) {
            throw new IllegalStateException("Application is shutting down.");
        }
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
            Execution execution = Execution.from(savedExecutionEntity);
            runningExecutions.add(execution);
            return execution;
        });
    }

    /**
     * Updates execution
     * @param execution execution
     */
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

    /**
     * Marks execution as success
     * @param execution execution
     */
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
        runningExecutions.remove(execution);
    }

    /**
     * Marks execution as failed
     * @param execution execution
     */
    public final void fail(Execution execution, Throwable e) {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager, transactionDefinition);
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            ExecutionEntity executionEntity = executionRepository.findById(execution.getExecutionId()).orElseThrow();
            executionEntity.setStatus(Execution.Status.FAILED);
            executionEntity.setUpdatedAt(Instant.now());
            executionEntity.setEndedAt(Instant.now());
            executionEntity.setMessage(ExceptionUtils.getRootCauseMessage(e));
            executionRepository.saveAndFlush(executionEntity);
        });
        runningExecutions.remove(execution);
    }

    /**
     * Marks execution as stopped
     * @param execution execution
     */
    public final void stop(Execution execution) {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager, transactionDefinition);
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            ExecutionEntity executionEntity = executionRepository.findById(execution.getExecutionId()).orElseThrow();
            executionEntity.setStatus(Execution.Status.STOPPED);
            executionEntity.setUpdatedAt(Instant.now());
            executionEntity.setEndedAt(Instant.now());
            executionRepository.saveAndFlush(executionEntity);
        });
        runningExecutions.remove(execution);
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

    /**
     * Gets execution
     * @param executionId execution id
     * @return execution
     */
    public Optional<Execution> getExecution(String executionId) {
        return executionRepository.findById(executionId)
                .map(Execution::from);
    }

}
