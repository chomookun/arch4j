package org.oopscraft.app4j.web;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

public class WebApplication {

    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(WebConfiguration.class)
                .beanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator())
                .web(WebApplicationType.SERVLET)
                .registerShutdownHook(true)
                .run(args);
    }

}
