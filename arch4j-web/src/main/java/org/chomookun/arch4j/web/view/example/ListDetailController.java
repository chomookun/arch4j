package org.chomookun.arch4j.web.view.example;

import org.chomookun.arch4j.core.example.model.Example;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/example/list-detail")
@PreAuthorize("hasAuthority('example')")
public class ListDetailController {

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("example/list-detail");
        modelAndView.addObject("_title", "Model And View Example");
        modelAndView.addObject("exampleTypes", Example.Type.values());
        return modelAndView;
    }

    @GetMapping("detail")
    public ModelAndView getExample(@RequestParam(value = "exampleId", required = false) String exampleId) {
        ModelAndView modelAndView = new ModelAndView("example/list-detail-detail");
        modelAndView.addObject("_title", "Model And View Example");
        modelAndView.addObject("exampleTypes", Example.Type.values());
        return modelAndView;
    }

}
