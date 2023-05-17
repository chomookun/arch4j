package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.code.Code;
import org.oopscraft.arch4j.core.code.CodeSearch;
import org.oopscraft.arch4j.core.code.CodeService;
import org.oopscraft.arch4j.web.exception.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("admin/code")
@RequiredArgsConstructor
//@PreAuthorize("hasAuthority('ADMIN_CODE')")
public class CodeController {

    private final CodeService codeService;

    /**
     * index
     * @return view
     */
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("admin/code.html");
    }

    /**
     * get code
     * @return code list
     */
    @GetMapping("get-codes")
    @ResponseBody
    public Page<Code> getCodes(CodeSearch codeSearch, Pageable pageable) {
        return codeService.getCodes(codeSearch, pageable);
    }

    /**
     * get property
     * @param id code id
     * @return code
     */
    @GetMapping("get-code")
    @ResponseBody
    public Code getCode(@RequestParam("id")String id) {
        return codeService.getCode(id)
                .orElseThrow(() -> new DataNotFoundException(id));
    }

    /**
     * saves code
     * @param code code info
     */
    @PostMapping("save-code")
    @ResponseBody
    public void saveCode(@RequestBody Code code) {
        codeService.saveCode(code);
    }

    /**
     * deletes code
     * @param id code id
     */
    @GetMapping("delete-code")
    @ResponseBody
    public void deleteCode(@RequestParam("id")String id) {
        codeService.deleteCode(id);
    }

}
