package org.chomookun.arch4j.core.notification.client.slack;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.chomookun.arch4j.core.notification.client.NotifierClient;
import org.chomookun.arch4j.core.common.support.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
public class SlackNotifierClient extends NotifierClient {

    private final String url;

    private final boolean insecure;

    public SlackNotifierClient(Properties config) {
        super(config);
        this.url = config.getProperty("url");
        this.insecure = Optional.ofNullable(config.getProperty("insecure"))
                .map(Boolean::parseBoolean)
                .orElse(false);
    }

    @Override
    public void sendMessage(String subject, String content, String receiver, Map<String,Object> option) {
        RestTemplate restTemplate = RestTemplateBuilder.create()
                .insecure(this.insecure)
                .build();
        Map<String, Object> payload = new LinkedHashMap<>();
        List<Map<String,Object>> blocks = new ArrayList<>();
        Map<String,Object> block = new LinkedHashMap<String,Object>(){{
            put("type", "section");
            put("text", new LinkedHashMap<String,Object>(){{
                put("type", "mrkdwn");
                put("text", StringUtils.abbreviate(subject + '\n' + Optional.ofNullable(content).orElse(""), 3000));
            }});
        }};
        blocks.add(block);
        payload.put("blocks", blocks);
        RequestEntity<Map<String,Object>> requestEntity = RequestEntity
                .post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload);

        // calls webhook
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            try {
                // response
                ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
                log.debug("Slack API response: {}", responseEntity.getBody());
                // breaks if successful
                break;
            } catch (HttpClientErrorException.TooManyRequests e) {
                log.warn("Slack API rate limit exceeded. Response: {}", e.getMessage());
                // checks Retry-After header
                Integer retryAfter = Optional.ofNullable(Objects.requireNonNull(e.getResponseHeaders()).getFirst("Retry-After"))
                        .map(Integer::parseInt)
                        .orElse(null);
                // if Retry-After header is present, waits for the specified time
                if (retryAfter != null) {
                    try {
                        Thread.sleep(retryAfter * 1000L);
                        continue;
                    } catch (Exception ignored) {}
                }
                // else throws exception
                else {
                    throw e;
                }
            }
        }
    }

}
