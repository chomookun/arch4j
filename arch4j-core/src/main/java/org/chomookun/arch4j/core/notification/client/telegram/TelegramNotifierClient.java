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

    private static final int TELEGRAM_LIMIT = 4000 - 20;

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
        RestTemplate restTemplate = RestTemplateBuilder.create().build();

        String text = String.format("%s\n\n%s", subject, content);
        List<String> parts = split(text, TELEGRAM_LIMIT);

        for (int i = 0; i < parts.size(); i++) {
            String part = parts.get(i);
            String sendText = parts.size() > 1
                    ? String.format("[%d/%d]\n%s", i + 1, parts.size(), part)
                    : part;
            Map<String, String> payload = new LinkedHashMap<>();
            payload.put("chat_id", chatId);
            payload.put("text", sendText);
            try {
                ResponseEntity<String> response = restTemplate.postForEntity(url, payload, String.class);
                log.debug("Telegram response: {}", response);
            } catch (Exception e) {
                log.warn(e.getMessage());
                throw new RuntimeException(e);
            } finally {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ignore) {
                }
            }
        }
    }

    private static List<String> split(String text, int limit) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < text.length(); i += limit) {
            result.add(StringUtils.substring(text, i, i + limit));
        }
        return result;
    }

}
