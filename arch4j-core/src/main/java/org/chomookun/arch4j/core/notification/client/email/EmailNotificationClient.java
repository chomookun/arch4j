package org.chomookun.arch4j.core.notification.client.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.chomookun.arch4j.core.common.support.RestTemplateBuilder;
import org.chomookun.arch4j.core.notification.client.NotificationClient;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
public class EmailNotificationClient extends NotificationClient {

    private final String host;

    private final int port;

    private final String username;

    private final String password;

    private final Properties properties;

    public EmailNotificationClient(Properties config) {
        super(config);
        this.host = config.getProperty("host");
        this.port = Integer.parseInt(config.getProperty("port"));
        this.username = config.getProperty("username");
        this.password = config.getProperty("password");
        // mail properties
        this.properties = new Properties();
        for (String key : properties.stringPropertyNames()) {
            if (key.startsWith("mail.")) {
                this.properties.put(key, properties.getProperty(key));
            }
        }
    }

    @Override
    public void sendMessage(String to, String subject, String content, Map<String,Object> option) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setJavaMailProperties(properties);
        // message
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            if (option != null) {
                if (option.containsKey("cc")) {
                    helper.setCc(option.get("cc").toString());
                }
            }
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        mailSender.send(mimeMessage);
    }

}
