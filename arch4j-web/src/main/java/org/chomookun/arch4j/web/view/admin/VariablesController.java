package org.chomookun.arch4j.web.view.admin;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.variable.model.Variable;
import org.chomookun.arch4j.core.variable.model.VariableSearch;
import org.chomookun.arch4j.core.variable.VariableService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;

@Controller
@RequestMapping("admin/variables")
@PreAuthorize("hasAuthority('admin.variables')")
@RequiredArgsConstructor
public class VariablesController {

    private final VariableService variableService;

    /**
     * Returns variables model and view
     * @return model and view
     */
    @GetMapping
    public ModelAndView variables() {
        return new ModelAndView("admin/variables.html");
    }

    /**
     * Returns variables
     * @param variableSearch variable search
     * @param pageable pageable
     * @return variables page
     */
    @GetMapping("get-variables")
    @ResponseBody
    public Page<Variable> getVariables(VariableSearch variableSearch, Pageable pageable) {
        return variableService.getVariables(variableSearch, pageable);
    }

    /**
     * Returns specified variable
     * @param variableId variable id
     * @return variable
     */
    @GetMapping("get-variable")
    @ResponseBody
    public Variable getVariable(@RequestParam("variableId")String variableId) {
        return variableService.getVariable(variableId)
                .orElseThrow();
    }

    /**
     * Saves variable
     * @param variable variable
     * @return saved variable
     */
    @PostMapping("save-variable")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.variables.edit')")
    public Variable saveVariable(@RequestBody @Valid Variable variable) {
        return variableService.saveVariable(variable);
    }

    /**
     * Deletes variable
     * @param variableId variable id
     */
    @GetMapping("delete-variable")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.variables.edit')")
    public void deleteVariable(@RequestParam("variableId")String variableId) {
        variableService.deleteVariable(variableId);
    }

}
