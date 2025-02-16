package org.chomookun.arch4j.web.view.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("login/reset-password")
public class ResetPasswordController {

    @GetMapping
    public ModelAndView resetPassword() {
        return new ModelAndView("login/reset-password.html");
    }

}
