package org.chomookun.arch4j.web.view.admin;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.variable.model.Variable;
import org.chomookun.arch4j.core.variable.model.VariableSearch;
import org.chomookun.arch4j.core.variable.VariableService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;

@Controller
@ConditionalOnProperty(prefix = "web.admin", name = "enabled", havingValue = "true", matchIfMissing = false)
@RequestMapping("admin/variable")
@PreAuthorize("hasAuthority('admin.variable')")
@RequiredArgsConstructor
public class VariableController {

    private final VariableService variableService;

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("admin/variable.html");
    }

    @GetMapping("get-variables")
    @ResponseBody
    public Page<Variable> getVariables(VariableSearch variableSearch, Pageable pageable) {
        return variableService.getVariables(variableSearch, pageable);
    }

    @GetMapping("get-variable")
    @ResponseBody
    public Variable getVariable(@RequestParam("variableId")String variableId) {
        return variableService.getVariable(variableId)
                .orElseThrow();
    }

    @PostMapping("save-variable")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.variable:edit')")
    public Variable saveVariable(@RequestBody @Valid Variable variable) {
        return variableService.saveVariable(variable);
    }

    @GetMapping("delete-variable")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.variable:edit')")
    public void deleteVariable(@RequestParam("variableId")String variableId) {
        variableService.deleteVariable(variableId);
    }

}
