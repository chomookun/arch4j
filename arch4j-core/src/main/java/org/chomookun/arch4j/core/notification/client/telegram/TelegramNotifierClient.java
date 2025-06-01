package org.chomookun.arch4j.core.notification.client.telegram;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.chomookun.arch4j.core.common.support.RestTemplateBuilder;
import org.chomookun.arch4j.core.notification.client.NotifierClient;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
public class TelegramNotifierClient extends NotifierClient {

    private final String botToken;

    private final String chatId;

    public TelegramNotifierClient(Properties config) {
        super(config);
        this.botToken = config.getProperty("bot-token");
        this.chatId = config.getProperty("chat-id");
    }

    @Override
    public void sendMessage(String subject, String content, String receiver, Map<String,Object> option) {
        String url = String.format("https://api.telegram.org/bot%s/sendMessage", botToken);
        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("chat_id", chatId);
        payload.put("text", String.format("%s\n\n%s", subject, content));
        try {
            RestTemplate restTemplate = RestTemplateBuilder.create().build();
            ResponseEntity<String> response = restTemplate.postForEntity(url, payload, String.class);
            log.debug("Telegram response: {}", response);
        } catch (Throwable t) {
            log.warn(t.getMessage());
        } finally {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ignore) {}
        }
    }

}
