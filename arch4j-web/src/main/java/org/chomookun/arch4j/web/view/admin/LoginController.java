package org.chomookun.arch4j.web.view.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ConditionalOnProperty(prefix = "web.admin", name = "enabled", havingValue = "true", matchIfMissing = false)
@RequestMapping("/admin/login")
@RequiredArgsConstructor
public class LoginController {

    @GetMapping
    public ModelAndView login() {
        return new ModelAndView("admin/login.html");
    }

}
