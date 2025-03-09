package org.chomookun.arch4j.web.view.apidocs;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.chomookun.arch4j.core.git.GitProperties;
import org.chomookun.arch4j.core.git.GitService;
import org.chomookun.arch4j.core.git.client.GitClient;
import org.chomookun.arch4j.core.git.model.Git;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;

@Controller
@RequestMapping("api-docs")
@PreAuthorize("hasAuthority('api-docs')")
@RequiredArgsConstructor
@Slf4j
public class ApiDocsController {

    @GetMapping
    public ModelAndView redirectToReDoc() {
        return new ModelAndView("apidocs/api-docs.html");
    }

}
