package org.chomookun.arch4j.core.notification.client.slack;

import org.chomookun.arch4j.core.notification.client.NotifierClient;
import org.chomookun.arch4j.core.notification.client.NotifierClientDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;

@Component
@Lazy(false)
public class SlackNotifierClientDefinition implements NotifierClientDefinition {

    @Override
    public String getClientType() {
        return "SLACK";
    }

    @Override
    public String getName() {
        return "Slack";
    }

    @Override
    public Class<? extends NotifierClient> getClassType() {
        return SlackNotifierClient.class;
    }

    @Override
    public String getPropertiesTemplate() {
        StringJoiner template = new StringJoiner("\n");
        template.add("url=url");
        template.add("insecure=true|false (default is false)");
        return template.toString();
    }

}
