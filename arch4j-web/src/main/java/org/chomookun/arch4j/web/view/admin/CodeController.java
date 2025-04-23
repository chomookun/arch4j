package org.chomookun.arch4j.web.view.admin;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.code.model.Code;
import org.chomookun.arch4j.core.code.model.CodeSearch;
import org.chomookun.arch4j.core.code.CodeService;
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
@RequestMapping("admin/code")
@PreAuthorize("hasAuthority('admin.code')")
@RequiredArgsConstructor
public class CodeController {

    private final CodeService codeService;

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("admin/code.html");
    }

    @GetMapping("get-codes")
    @ResponseBody
    public Page<Code> getCodes(CodeSearch codeSearch, Pageable pageable) {
        return codeService.getCodes(codeSearch, pageable);
    }

    @GetMapping("get-code")
    @ResponseBody
    public Code getCode(@RequestParam("codeId")String codeId) {
        return codeService.getCode(codeId)
                .orElseThrow();
    }

    @PostMapping("save-code")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.code:edit')")
    @Transactional
    public Code saveCode(@RequestBody Code code) {
        return codeService.saveCode(code);
    }

    @GetMapping("delete-code")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.code:edit')")
    @Transactional
    public void deleteCode(@RequestParam("codeId")String codeId) {
        codeService.deleteCode(codeId);
    }

}
