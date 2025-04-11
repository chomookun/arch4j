package org.chomookun.arch4j.web.view.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("admin")
@PreAuthorize("hasAuthority('admin')")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        RedirectView redirectView = new RedirectView("admin/monitor");
        redirectView.setExposeModelAttributes(false);
        modelAndView.setView(redirectView);
        return modelAndView;
    }

}
