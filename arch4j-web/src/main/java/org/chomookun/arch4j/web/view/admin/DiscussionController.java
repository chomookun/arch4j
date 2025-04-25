package org.chomookun.arch4j.web.view.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.discussion.DiscussionService;
import org.chomookun.arch4j.core.discussion.model.Discussion;
import org.chomookun.arch4j.core.discussion.model.DiscussionSearch;
import org.chomookun.arch4j.core.discussion.provider.DiscussionProviderDefinitionRegistry;
import org.chomookun.arch4j.core.storage.client.StorageClientDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ConditionalOnProperty(prefix = "web.admin", name = "enabled", havingValue = "true", matchIfMissing = false)
@RequestMapping("admin/discussion")
@PreAuthorize("hasAuthority('admin.discussion')")
@RequiredArgsConstructor
public class DiscussionController {

    private final DiscussionService discussionService;

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("admin/discussion");
        modelAndView.addObject("discussionProviderDefinitions", DiscussionProviderDefinitionRegistry.getDiscussionProviderDefinitions());
        return modelAndView;
    }

    @GetMapping("get-discussions")
    @ResponseBody
    public Page<Discussion> getDiscussions(DiscussionSearch discussionSearch, Pageable pageable) {
        return discussionService.getDiscussions(discussionSearch, pageable);
    }

    @GetMapping("get-discussion")
    @ResponseBody
    public Discussion getDiscussion(@RequestParam("discussionId") String discussionId) {
        return discussionService.getDiscussion(discussionId).orElseThrow();
    }

    @PostMapping("save-discussion")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.discussion:edit')")
    public Discussion saveDiscussion(@RequestBody @Valid Discussion discussion) {
        return discussionService.saveDiscussion(discussion);
    }

    @GetMapping("delete-discussion")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.discussion:edit')")
    public void deleteVariable(@RequestParam("discussionId") String discussionId) {
        discussionService.deleteDiscussion(discussionId);
    }

}
