package org.chomookun.arch4j.web.view.admin;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.page.model.Page;
import org.chomookun.arch4j.core.page.model.PageSearch;
import org.chomookun.arch4j.core.page.PageService;
import org.chomookun.arch4j.core.page.model.PageWidget;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;

@Controller
@RequestMapping("admin/page")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('admin.page')")
public class PageController {

    private final PageService pageService;

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("admin/page.html");
        modelAndView.addObject("pageWidgetDefinitions", pageService.getPageWidgetDefinitions());
        return modelAndView;
    }

    @GetMapping("get-pages")
    @ResponseBody
    public PageImpl<Page> getPages(PageSearch pageSearch, Pageable pageable) {
        return pageService.getPages(pageSearch, pageable);
    }

    @GetMapping("get-page")
    @ResponseBody
    public Page getPage(@RequestParam("pageId")String pageId) {
        return pageService.getPage(pageId)
                .orElseThrow();
    }

    @PostMapping("save-page")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.page:edit')")
    public Page savePage(@RequestBody @Valid Page page) {
        return pageService.savePage(page);
    }

    @GetMapping("delete-page")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.page:edit')")
    public void deletePage(@RequestParam("pageId")String pageId) {
        pageService.deletePage(pageId);
    }

    @PostMapping("get-page-widget-url")
    @ResponseBody
    public PageWidget getPageWidgetUrl(@RequestBody PageWidget pageWidget) {
        String url = pageService.getPageWidgetUrl(pageWidget);
        pageWidget.setUrl(url);
        return pageWidget;
    }

}
