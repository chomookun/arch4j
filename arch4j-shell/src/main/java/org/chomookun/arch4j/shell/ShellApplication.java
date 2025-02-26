package org.chomookun.arch4j.shell;

import org.chomookun.arch4j.core.common.cli.SpringApplicationInstaller;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

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
            System.exit(0);
        }

        // runs shell application
        new SpringApplicationBuilder(ShellApplication.class)
                .web(WebApplicationType.NONE)
                .registerShutdownHook(true)
                .run(args);
    }

}
