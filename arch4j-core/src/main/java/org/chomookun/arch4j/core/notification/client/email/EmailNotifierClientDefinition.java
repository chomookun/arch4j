package org.chomookun.arch4j.core.notification.client.email;

import org.chomookun.arch4j.core.notification.client.NotifierClient;
import org.chomookun.arch4j.core.notification.client.NotifierClientDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;

@Component
@Lazy(false)
public class EmailNotifierClientDefinition implements NotifierClientDefinition {

    @Override
    public String getClientType() {
        return "EMAIL";
    }

    @Override
    public String getName() {
        return "Email";
    }

    @Override
    public Class<? extends NotifierClient> getTypeClass() {
        return EmailNotifierClient.class;
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

}
