package org.chomookun.arch4j.shell;

import org.chomookun.arch4j.shell.common.SpringApplicationInstaller;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class ShellApplication {

    /**
     * runs application
     * @param args arguments
     */
    public static void main(String[] args) {
        // install
        if(Arrays.asList(args).contains("install")) {
            SpringApplicationInstaller.install(ShellApplication.class, args);
        }
        // runs shell application
        new SpringApplicationBuilder(ShellApplication.class)
                .web(WebApplicationType.NONE)
                .registerShutdownHook(true)
                .run(args);
    }

}
