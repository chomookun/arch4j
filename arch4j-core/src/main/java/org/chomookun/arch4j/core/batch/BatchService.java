package org.chomookun.arch4j.core.batch;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.batch.model.JobExecution;
import org.chomookun.arch4j.core.batch.model.Job;
import org.chomookun.arch4j.core.batch.model.JobInstance;
import org.chomookun.arch4j.core.batch.model.JobSearch;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final JobExplorer jobExplorer;

    /**
     * Gets jobs
     * @param jobSearch job search
     * @param pageable pageable
     * @return page of jobs
     */
    public Page<Job> getJobs(JobSearch jobSearch, Pageable pageable) {
        List<Job> jobs = new ArrayList<>();
        List<String> jobNames = jobExplorer.getJobNames();
        for (String jobName : jobNames) {
            long instanceCount;
            try {
                instanceCount = jobExplorer.getJobInstanceCount(jobName);
            } catch (NoSuchJobException e) {
                instanceCount = 0L;
            }
            org.springframework.batch.core.JobInstance lastJobInstance = jobExplorer.getLastJobInstance(jobName);
            LocalDateTime lastStartTime = null;
            LocalDateTime lastEndTime = null;
            BatchStatus lastBatchStatus = null;
            ExitStatus lastExitStatus = null;
            if (lastJobInstance != null) {
                org.springframework.batch.core.JobExecution lastJobExecution = jobExplorer.getLastJobExecution(lastJobInstance);
                if (lastJobExecution != null) {
                    lastStartTime = lastJobExecution.getStartTime();
                    lastEndTime = lastJobExecution.getEndTime();
                    lastBatchStatus = lastJobExecution.getStatus();
                    lastExitStatus = lastJobExecution.getExitStatus();
                }
            }
            Job jobInfo= Job.builder()
                    .jobName(jobName)
                    .instanceCount(instanceCount)
                    .lastStartTime(lastStartTime)
                    .lastEndTime(lastEndTime)
                    .lastBatchStatus(lastBatchStatus)
                    .lastExitStatus(lastExitStatus)
                    .build();
            jobs.add(jobInfo);
        }
        // filter and return
        jobs = jobs.stream()
                .filter(job -> {
                    if (jobSearch.getJobName() != null) {
                        return job.getJobName().contains(jobSearch.getJobName());
                    }
                    if (jobSearch.getLastBatchStatus() != null) {
                        return job.getLastBatchStatus().equals(jobSearch.getLastBatchStatus());
                    }
                    return true;
                })
                .toList();
        jobs = jobs.subList((int)pageable.getOffset(), (int)Math.min(jobs.size(), pageable.getOffset() + pageable.getPageSize()));
        return new PageImpl<>(jobs, pageable, jobs.size());
    }

    /**
     * Gets job instances
     * @param jobName job name
     * @param pageable pageable
     * @return page of job instances
     */
    public Page<JobInstance> getJobInstances(String jobName, Pageable pageable) {
        List<JobInstance> jobInstances = new ArrayList<>();
        long jobInstanceCount;
        try {
            jobInstanceCount = jobExplorer.getJobInstanceCount(jobName);
        } catch (NoSuchJobException e) {
            jobInstanceCount = 0L;
        }
        List<org.springframework.batch.core.JobInstance> springJobInstances = jobExplorer.getJobInstances(jobName, (int)pageable.getOffset(), pageable.getPageSize());
        for (org.springframework.batch.core.JobInstance springJobInstance : springJobInstances) {
            List<org.springframework.batch.core.JobExecution> springJobExecutions = jobExplorer.getJobExecutions(springJobInstance);
            org.springframework.batch.core.JobExecution lastSpringJobExecution = jobExplorer.getLastJobExecution(springJobInstance);
            JobInstance jobInstance = JobInstance.builder()
                    .jobName(springJobInstance.getJobName())
                    .instanceId(springJobInstance.getInstanceId())
                    .executionCount(springJobExecutions.size())
                    .startTime(lastSpringJobExecution.getStartTime())
                    .endTime(lastSpringJobExecution.getEndTime())
                    .batchStatus(lastSpringJobExecution.getStatus())
                    .exitStatus(lastSpringJobExecution.getExitStatus())
                    .build();
            jobInstances.add(jobInstance);
        }
        return new PageImpl<>(jobInstances, pageable, jobInstanceCount);
    }

    /**
     * Gets job executions
     * @param instanceId job instance id
     * @return list of job executions
     */
    public List<JobExecution> getJobExecutions(long instanceId) {
        org.springframework.batch.core.JobInstance jobInstance = jobExplorer.getJobInstance(instanceId);
        List<org.springframework.batch.core.JobExecution> jobExecutions = jobExplorer.getJobExecutions(jobInstance);
        List<JobExecution> jobExecutionInfos = new ArrayList<>();
        for (org.springframework.batch.core.JobExecution jobExecution : jobExecutions) {
            JobExecution jobExecutionInfo = JobExecution.builder()
                    .jobName(jobExecution.getJobInstance().getJobName())
                    .instanceId(jobExecution.getJobInstance().getInstanceId())
                    .startTime(jobExecution.getStartTime())
                    .endTime(jobExecution.getEndTime())
                    .status(jobExecution.getStatus())
                    .exitStatus(jobExecution.getExitStatus())
                    .build();
            jobExecutionInfos.add(jobExecutionInfo);
        }
        return jobExecutionInfos;
    }

}
