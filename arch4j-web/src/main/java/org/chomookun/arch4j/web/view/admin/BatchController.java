package org.chomookun.arch4j.web.view.admin;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.batch.BatchService;
import org.chomookun.arch4j.core.batch.model.JobExecution;
import org.chomookun.arch4j.core.batch.model.Job;
import org.chomookun.arch4j.core.batch.model.JobInstance;
import org.chomookun.arch4j.core.batch.model.JobSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("admin/batch")
@PreAuthorize("hasAuthority('admin.batch')")
@RequiredArgsConstructor
public class BatchController {

    private final BatchService batchService;

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("admin/batch.html");
    }

    @GetMapping("get-jobs")
    @ResponseBody
    public Page<Job> getJobs(JobSearch jobSearch, Pageable pageable) {
        return batchService.getJobs(jobSearch, pageable);
    }

    @GetMapping("get-job-instances")
    @ResponseBody
    public Page<JobInstance> getJobInstances(@RequestParam("jobName") String jobName, Pageable pageable) {
        return batchService.getJobInstances(jobName, pageable);
    }

    @GetMapping("get-job-executions")
    @ResponseBody
    public List<JobExecution> getJobExecutions(@RequestParam("instanceId") Long instanceId) {
        return batchService.getJobExecutions(instanceId);
    }

}
