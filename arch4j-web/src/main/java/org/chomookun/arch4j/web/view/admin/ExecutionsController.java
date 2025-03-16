package org.chomookun.arch4j.web.view.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.execution.model.Execution;
import org.chomookun.arch4j.core.execution.model.ExecutionSearch;
import org.chomookun.arch4j.core.execution.ExecutionService;
import org.chomookun.arch4j.core.git.model.Git;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("admin/executions")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('admin.executions')")
@Slf4j
public class ExecutionsController {

    private final ExecutionService executionService;

    @GetMapping
    public ModelAndView executions() {
        ModelAndView modelAndView = new ModelAndView("admin/executions.html");
        modelAndView.addObject("executionStatuses", Execution.Status.values());
        return modelAndView;
    }

    @GetMapping("get-executions")
    @ResponseBody
    public Page<Execution> getExecutions(ExecutionSearch executionSearch, Pageable pageable) {
        return executionService.getExecutions(executionSearch, pageable);
    }

    @GetMapping("get-execution")
    @ResponseBody
    public Execution getExecution(@RequestParam("executionId") String executionId) {
        return executionService.getExecution(executionId).orElseThrow();
    }

    @PostMapping("save-execution")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.executions.edit')")
    public Execution saveExecution(@RequestBody @Valid Execution execution) {
        return executionService.saveExecution(execution);
    }

    @DeleteMapping("delete-execution")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.executions.edit')")
    public void deleteExecution(@RequestParam("executionId") String executionId) {
        executionService.deleteExecution(executionId);
    }

}
