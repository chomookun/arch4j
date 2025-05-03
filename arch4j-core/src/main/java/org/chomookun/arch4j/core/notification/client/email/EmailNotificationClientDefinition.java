package org.chomookun.arch4j.core.notification.client.email;

import org.chomookun.arch4j.core.notification.client.NotificationClient;
import org.chomookun.arch4j.core.notification.client.NotificationClientDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Component
@Lazy(false)
public class EmailNotificationClientDefinition implements NotificationClientDefinition {

    @Override
    public String getClientId() {
        return "EMAIL";
    }

    @Override
    public String getName() {
        return "Email";
    }

    @Override
    public String getConfigTemplate() {
        StringJoiner template = new StringJoiner("\n");
        template.add("host=[smtp.example.com]");
        template.add("port=[465]");
        template.add("username=[username]");
        template.add("password=[password]");
        template.add("auth=true|false (default is false)");
        template.add("starttls=true|false (default is true)");
        return template.toString();
    }

    @Override
    public Class<? extends NotificationClient> getClassType() {
        return EmailNotificationClient.class;
    }

}
