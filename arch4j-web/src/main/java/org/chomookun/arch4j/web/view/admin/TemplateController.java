package org.chomookun.arch4j.web.view.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.email.model.Email;
import org.chomookun.arch4j.core.email.model.EmailSearch;
import org.chomookun.arch4j.core.template.TemplateService;
import org.chomookun.arch4j.core.template.model.Template;
import org.chomookun.arch4j.core.template.model.TemplateSearch;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ConditionalOnProperty(prefix = "web.admin", name = "enabled", havingValue = "true", matchIfMissing = false)
@RequestMapping("admin/template")
@PreAuthorize("hasAuthority('admin.template')")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("admin/template");
    }

    @GetMapping("get-templates")
    @ResponseBody
    public Page<Template> getTemplates(TemplateSearch templateSearch, Pageable pageable) {
        return templateService.getTemplates(templateSearch, pageable);
    }

    @GetMapping("get-template")
    @ResponseBody
    public Template getTemplate(@RequestParam("templateId")String templateId) {
        return templateService.getTemplate(templateId)
                .orElseThrow();
    }

    @PostMapping("save-template")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.template:edit')")
    public Template saveTemplate(@RequestBody @Valid Template template) {
        return templateService.saveTemplate(template);
    }

    @GetMapping("delete-template")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.template:edit')")
    public void deleteEmail(@RequestParam("templateId")String templateId) {
        templateService.deleteTemplate(templateId);
    }

}
