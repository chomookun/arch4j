package org.chomookun.arch4j.core.common.cli;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SpringApplicationInstaller {

//    public static enum InstallMode {
//        I("(Re)Install (Caution: all data will be removed."),
//        U("Update");
//        private String message;
//        InstallMode(String message) {
//            this.message = message;
//        }
//    }
//    static enum InstallMode { I, U }

    public static void install(Class<?> applicationClass, String[] args) {
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
        new SpringApplicationBuilder(applicationClass)
                .beanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator())
                .web(WebApplicationType.NONE)
                .bannerMode(Banner.Mode.OFF)
                .registerShutdownHook(true)
                .run(args);
    }

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
