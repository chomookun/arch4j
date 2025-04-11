package org.chomookun.arch4j.web.view.admin;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.git.model.Git;
import org.chomookun.arch4j.core.git.model.GitSearch;
import org.chomookun.arch4j.core.git.GitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;

@Controller
@RequestMapping("admin/git")
@PreAuthorize("hasAuthority('admin.git')")
@RequiredArgsConstructor
public class GitController {

    private final GitService gitService;

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("admin/git.html");
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
    @PreAuthorize("hasAuthority('admin.git:edit')")
    public Git saveGit(@RequestBody @Valid Git git) {
        return gitService.saveGit(git);
    }

    @GetMapping("delete-git")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.git:edit')")
    public void deleteGit(@RequestParam("gitId")String gitId) {
        gitService.deleteGit(gitId);
    }

}
