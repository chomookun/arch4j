package org.chomoo.arch4j.web.admin.view;

import lombok.RequiredArgsConstructor;
import org.chomoo.arch4j.core.git.model.Git;
import org.chomoo.arch4j.core.git.model.GitSearch;
import org.chomoo.arch4j.core.git.service.GitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;

@Controller
@RequestMapping("admin/gits")
@PreAuthorize("hasAuthority('admin.gits')")
@RequiredArgsConstructor
public class GitsController {

    private final GitService gitService;

    @GetMapping
    public ModelAndView gits() {
        return new ModelAndView("admin/gits.html");
    }

    @GetMapping("get-gits")
    @ResponseBody
    public Page<Git> getGits(GitSearch gitSearch, Pageable pageable) {
        return gitService.getGits(gitSearch, pageable);
    }

    @GetMapping("get-git")
    @ResponseBody
    public Git getGit(@RequestParam("gitId")String gitId) {
        return gitService.getGit(gitId).orElseThrow();
    }

    @PostMapping("save-git")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.gits')")
    public Git saveGit(@RequestBody @Valid Git git) {
        return gitService.saveGit(git);
    }

    @GetMapping("delete-git")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.gits.edit')")
    public void deleteGit(@RequestParam("gitId")String gitId) {
        gitService.deleteGit(gitId);
    }

}
