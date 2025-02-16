package org.chomookun.arch4j.web;

import org.chomookun.arch4j.core.common.cli.SpringApplicationInstaller;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

import java.util.Arrays;

@SpringBootApplication(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
public class WebApplication {

    /**
     * Runs web application
     * @param args arguments
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(WebApplication.class)
                .web(WebApplicationType.SERVLET)
                .registerShutdownHook(true)
                .run(args);
    }

}
