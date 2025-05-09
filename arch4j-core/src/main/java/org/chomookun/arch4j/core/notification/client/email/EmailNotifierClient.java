package org.chomookun.arch4j.core.notification.client.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.notification.client.NotifierClient;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.*;

@Slf4j
public class EmailNotifierClient extends NotifierClient {

    private final String host;

    private final int port;

    private final String username;

    private final String password;

    private final Properties properties;

    public EmailNotifierClient(Properties config) {
        super(config);
        this.host = config.getProperty("host");
        this.port = Integer.parseInt(config.getProperty("port"));
        this.username = config.getProperty("username");
        this.password = config.getProperty("password");
        // mail properties
        this.properties = new Properties();
        for (String key : config.stringPropertyNames()) {
            if (key.startsWith("mail.")) {
                this.properties.put(key, config.getProperty(key));
            }
        }
    }

    @Override
    public void sendMessage(String subject, String content, String receiver, Map<String,Object> option) {
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
            helper.setTo(receiver);
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
