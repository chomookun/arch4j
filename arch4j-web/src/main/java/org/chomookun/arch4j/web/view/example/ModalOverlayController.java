package org.chomookun.arch4j.web.view.example;

import org.chomookun.arch4j.core.example.model.Example;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/example/modal-overlay")
@PreAuthorize("hasAuthority('example')")
public class ModalOverlayController {

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("example/modal-overlay");
        modelAndView.addObject("_title", "Modal Overlay Example");
        modelAndView.addObject("exampleTypes", Example.Type.values());
        return modelAndView;
    }

}
