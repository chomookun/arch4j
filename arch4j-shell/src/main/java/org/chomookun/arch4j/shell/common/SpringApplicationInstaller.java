package org.chomookun.arch4j.shell.common;

import org.apache.commons.lang3.ArrayUtils;
import org.chomookun.arch4j.shell.ShellApplication;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

import java.util.LinkedHashMap;
import java.util.Map;

public class SpringApplicationInstaller {

    /**
     * Installs application
     * @param applicationClass application class
     * @param args args
     */
    public static void install(Class<?> applicationClass, String[] args) {
        // disabled properties
        args = replaceOrAdd(args, "--spring.shell.interactive.enabled=false");
        // check install mode
        Map<String,String> installModes = new LinkedHashMap<>() {{
            put("I", "(Re)Install (Caution: all data will be removed.");
            put("U", "Update");
        }};
        String installMode = InteractiveUtil.askSelect("Select Installation Mode", installModes);

        // create schema
        String driverClassName = InteractiveUtil.askInput("Driver Class Name");
        String jdbcUrl = InteractiveUtil.askInput("Jdbc Url");
        String username = InteractiveUtil.askInput("Username");
        String password = InteractiveUtil.askInput("Password");
        InteractiveUtil.askConfirm("Continue to create schema?");

        // data source properties
        args = replaceOrAdd(args, String.format("--spring.datasource.hikari.driver-class-name=%s", driverClassName));
        args = replaceOrAdd(args, String.format("--spring.datasource.hikari.jdbc-url=%s", jdbcUrl));
        args = replaceOrAdd(args, String.format("--spring.datasource.hikari.username=%s", username));
        args = replaceOrAdd(args, String.format("--spring.datasource.hikari.password=%s", password));

        // sets spring boot properties for initialization
        args = replaceOrAdd(args, "--logging.level.root=DEBUG");
        args = replaceOrAdd(args, "--logging.pattern.console=%msg%n");

        // database option by install mode
        if("I".equals(installMode)) {
            args = replaceOrAdd(args, "--spring.sql.init.mode=always");
            args = replaceOrAdd(args, "--spring.jpa.hibernate.ddl-auto=create");
        }else if("U".equals(installMode)) {
            args = replaceOrAdd(args, "--spring.sql.init.mode=never");
            args = replaceOrAdd(args, "--spring.jpa.hibernate.ddl-auto=update");
        }

        // creates jdbc session table
        args = replaceOrAdd(args, "--spring.main.web-application-type=servlet");
        args = replaceOrAdd(args, "--spring.session.store-type=jdbc");
        args = replaceOrAdd(args, "--spring.session.jdbc.initialize-schema=always");

        // launch spring boot application
        ApplicationContext applicationContext = new SpringApplicationBuilder(applicationClass)
                .beanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator())
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .registerShutdownHook(true)
                .run(args);
        System.exit(SpringApplication.exit(applicationContext));
    }

    /**
     * Replaces or adds argument
     * @param args arguments
     * @param argument argument
     * @return arguments
     */
    static String[] replaceOrAdd(String[] args, String argument) {
        String[] argumentPair = argument.split("=");
        String key = argumentPair[0];
        String value = argumentPair[1];
        // replace
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith(key + "=")) {
                args[i] = argument;
                return args;
            }
        }
        // add
        return ArrayUtils.add(args, argument);
    }

}
