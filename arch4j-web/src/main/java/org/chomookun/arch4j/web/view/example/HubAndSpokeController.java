package org.chomookun.arch4j.web.view.example;

import jakarta.servlet.http.HttpServletResponse;
import org.chomookun.arch4j.core.example.model.Example;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/example/hub-and-spoke")
@PreAuthorize("hasAuthority('example')")
public class HubAndSpokeController {

    /**
     * Examples
     * @return model and view
     */
    @GetMapping("examples")
    public ModelAndView examples() {
        ModelAndView modelAndView = new ModelAndView("example/hub-and-spoke/examples.html");
        modelAndView.addObject("_title", "Model And View Example");
        modelAndView.addObject("exampleTypes", Example.Type.values());
        return modelAndView;
    }

    /**
     * Example
     * @return model and view
     */
    @GetMapping("example")
    public ModelAndView example(@RequestParam(value = "exampleId", required = false) String exampleId) {
        ModelAndView modelAndView = new ModelAndView("example/hub-and-spoke/example.html");
        modelAndView.addObject("_title", "Model And View Example");
        modelAndView.addObject("exampleTypes", Example.Type.values());
        return modelAndView;
    }

}
