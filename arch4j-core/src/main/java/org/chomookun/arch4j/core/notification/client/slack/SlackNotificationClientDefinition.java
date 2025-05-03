package org.chomookun.arch4j.core.notification.client.slack;

import org.chomookun.arch4j.core.notification.client.NotificationClient;
import org.chomookun.arch4j.core.notification.client.NotificationClientDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Component
@Lazy(false)
public class SlackNotificationClientDefinition implements NotificationClientDefinition {

    @Override
    public String getClientId() {
        return "SLACK";
    }

    @Override
    public String getName() {
        return "Slack";
    }

    @Override
    public String getConfigTemplate() {
        StringJoiner template = new StringJoiner("\n");
        template.add("url=url");
        template.add("insecure=true|false (default is false)");
        return template.toString();
    }

    @Override
    public Class<? extends NotificationClient> getClassType() {
        return SlackNotificationClient.class;
    }

}
