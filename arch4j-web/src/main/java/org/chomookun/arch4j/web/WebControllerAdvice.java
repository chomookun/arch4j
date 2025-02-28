package org.chomookun.arch4j.web;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.CoreProperties;
import org.chomookun.arch4j.core.security.SecurityProperties;
import org.chomookun.arch4j.core.security.support.SecurityUtils;
import org.chomookun.arch4j.core.user.model.User;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@ControllerAdvice
@RequiredArgsConstructor
public class WebControllerAdvice {

    private static final long SCRIPT_VERSION = System.currentTimeMillis();

    private final CoreProperties coreProperties;

    private final WebProperties webProperties;

    private final SecurityProperties securityProperties;

    private final HttpSession httpSession;

    /**
     * check if the request is rest controller
     * ps. @ControllerAdvice is not working for annotationTypes because of @RestController extends from @Controller
     * @param request http servlet request
     * @return true if the request is rest controller
     */
    private boolean isRestControllerBeanType(HttpServletRequest request) {
        Object handler = request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);
        if (handler instanceof HandlerMethod handlerMethod) {
            return AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), RestController.class) != null;
        }
        return false;
    }

    @GetMapping
    public ModelAndView index() {
        // redirect index
        RedirectView redirectView = new RedirectView(webProperties.getIndex());
        return new ModelAndView(redirectView);
    }

    @ModelAttribute("_securityPolicy")
    public String securityPolicy() {
        return Optional.ofNullable(securityProperties.getSecurityPolicy())
                .map(Enum::name)
                .orElse(null);
    }

    @ModelAttribute("_scriptVersion")
    public String scriptVersion() {
        return String.valueOf(SCRIPT_VERSION);
    }

    @ModelAttribute("_sessionTimeout")
    public Integer sessionTimeout() {
        return httpSession.getMaxInactiveInterval();
    }

    @ModelAttribute("_theme")
    public String theme() {
        return webProperties.getTheme();
    }

    @ModelAttribute("_brand")
    public String brand() {
        return webProperties.getBrand();
    }

    @ModelAttribute("_favicon")
    public String favicon() {
        return webProperties.getFavicon();
    }

    @ModelAttribute("_title")
    public String title() {
        return webProperties.getTitle();
    }

    @ModelAttribute("_locales")
    public List<Map<String,String>> locales() {
        return coreProperties.getSupportedLocales().stream()
                .map(locale -> new LinkedHashMap<String,String>(){{
                    put("language", locale.getLanguage());
                    put("displayLanguage", locale.getDisplayLanguage(locale));
                    put("country", locale.getCountry());
                    put("displayCountry", locale.getDisplayCountry(locale));
                }})
                .collect(Collectors.toList());
    }

    @ModelAttribute("_locale")
    public Locale getLocale(HttpServletRequest request) {
        return Objects.requireNonNull(RequestContextUtils.getLocaleResolver(request)).resolveLocale(request);
    }

    @ModelAttribute("_user")
    public User getUser(HttpServletRequest request) {
        if (isRestControllerBeanType(request)) {
            return null;
        }
        return SecurityUtils.getCurrentUser()
                .orElse(new User());
    }

}
