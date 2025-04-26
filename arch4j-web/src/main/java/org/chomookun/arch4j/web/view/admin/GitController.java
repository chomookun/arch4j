package org.chomookun.arch4j.web.view.admin;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.discussion.DiscussionService;
import org.chomookun.arch4j.core.discussion.model.Discussion;
import org.chomookun.arch4j.core.discussion.model.DiscussionSearch;
import org.chomookun.arch4j.core.git.model.Git;
import org.chomookun.arch4j.core.git.model.GitSearch;
import org.chomookun.arch4j.core.git.GitService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;

import java.util.List;

@Controller
@ConditionalOnProperty(prefix = "web.admin", name = "enabled", havingValue = "true", matchIfMissing = false)
@RequestMapping("admin/git")
@PreAuthorize("hasAuthority('admin.git')")
@RequiredArgsConstructor
public class GitController {

    private final GitService gitService;

    private final DiscussionService discussionService;

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("admin/git");
        // discussions
        List<Discussion> discussions = discussionService.getDiscussions(DiscussionSearch.builder().build(), Pageable.unpaged()).getContent();
        modelAndView.addObject("discussions", discussions);
        return modelAndView;
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
