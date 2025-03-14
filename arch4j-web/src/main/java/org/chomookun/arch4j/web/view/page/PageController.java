package org.chomookun.arch4j.web.view.page;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.page.model.Page;
import org.chomookun.arch4j.core.page.PageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("page")
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;

    @GetMapping("{pageId}")
    public ModelAndView page(@PathVariable("pageId")String pageId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("page/page.html");
        Page page = pageService.getPage(pageId).orElseThrow();
        modelAndView.addObject("_title", page.getName());
        modelAndView.addObject("page", page);
        return modelAndView;
    }

}
