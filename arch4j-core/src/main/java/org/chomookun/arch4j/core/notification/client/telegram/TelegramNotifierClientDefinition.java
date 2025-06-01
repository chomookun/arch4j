package org.chomookun.arch4j.core.notification.client.telegram;

import org.chomookun.arch4j.core.notification.client.NotifierClient;
import org.chomookun.arch4j.core.notification.client.NotifierClientDefinition;
import org.chomookun.arch4j.core.notification.client.slack.SlackNotifierClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;

@Component
@Lazy(false)
public class TelegramNotifierClientDefinition implements NotifierClientDefinition {

    @Override
    public String getClientType() {
        return "TELEGRAM";
    }

    @Override
    public String getName() {
        return "Telegram";
    }

    @Override
    public Class<? extends NotifierClient> getClassType() {
        return TelegramNotifierClient.class;
    }

    @Override
    public String getPropertiesTemplate() {
        StringJoiner template = new StringJoiner("\n");
        template.add("bot-token={bot-token}");
        template.add("chat-id={chat-id}");
        return template.toString();
    }

}
