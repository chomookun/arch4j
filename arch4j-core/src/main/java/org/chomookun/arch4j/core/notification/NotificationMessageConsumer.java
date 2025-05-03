package org.chomookun.arch4j.core.notification;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.notification.client.NotificationClient;
import org.chomookun.arch4j.core.notification.client.NotificationClientFactory;
import org.chomookun.arch4j.core.notification.entity.NotificationMessageEntity;
import org.chomookun.arch4j.core.notification.model.Notification;
import org.chomookun.arch4j.core.notification.model.NotificationMessage;
import org.chomookun.arch4j.core.notification.repository.NotificationMessageRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@Lazy(false)
@RequiredArgsConstructor
@Slf4j
public class NotificationMessageConsumer {

    private final BlockingQueue<NotificationMessage> queue = new LinkedBlockingQueue<>();

    private final NotificationService notificationService;

    private final NotificationMessageRepository notificationMessageRepository;

    private final PlatformTransactionManager transactionManager;

    /**
     * Initializes the consumer thread
     */
    @PostConstruct
    public void init() {
        Thread thread = new Thread(this::consumeNotificationMessage, this.getClass().getSimpleName());
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Adds notification message to queue
     * @param notificationMessage notification message
     */
    public void addNotificationMessage(NotificationMessage notificationMessage) {
        NotificationMessageEntity notificationMessageEntity = NotificationMessageEntity.builder()
                .messageId(notificationMessage.getMessageId())
                .notificationId(notificationMessage.getNotificationId())
                .notificationName(notificationMessage.getNotificationName())
                .to(notificationMessage.getTo())
                .subject(notificationMessage.getSubject())
                .content(notificationMessage.getContent())
                .submittedAt(Instant.now())
                .status(NotificationMessage.Status.SUBMITTED)
                .build();
        try {
            queue.add(notificationMessage);
        } catch (Throwable t) {
            log.warn(t.getMessage());
            notificationMessageEntity.setStatus(NotificationMessage.Status.FAILED);
            notificationMessageEntity.setErrorMessage(t.getMessage());
        } finally {
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager, def);
            transactionTemplate.executeWithoutResult(status -> {
                notificationMessageRepository.saveAndFlush(notificationMessageEntity);
            });
        }
    }

    /**
     * Consume notification message
     */
    public void consumeNotificationMessage() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                NotificationMessage notificationMessage = queue.take();
                notificationMessage.setSentAt(Instant.now());
                try {
                    Notification notification = notificationService.getNotification(notificationMessage.getNotificationId()).orElseThrow();
                    NotificationClient notificationClient = NotificationClientFactory.getNotificationClient(notification);
                    notificationClient.sendMessage(notificationMessage.getTo(), notificationMessage.getSubject(), notificationMessage.getContent(), notificationMessage.getOption());
                    notificationMessage.setStatus(NotificationMessage.Status.COMPLETED);
                } catch (Throwable t) {
                    log.warn(t.getMessage());
                    notificationMessage.setStatus(NotificationMessage.Status.FAILED);
                    notificationMessage.setErrorMessage(t.getMessage());
                    throw t;
                } finally {
                    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                    TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager, def);
                    transactionTemplate.executeWithoutResult(status -> {
                        notificationMessageRepository.updateResult(
                                notificationMessage.getMessageId(),
                                notificationMessage.getSentAt(),
                                notificationMessage.getStatus(),
                                notificationMessage.getErrorMessage());
                    });
                }
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
