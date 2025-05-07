package org.chomookun.arch4j.core.notification;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.notification.client.NotifierClient;
import org.chomookun.arch4j.core.notification.client.NotifierClientFactory;
import org.chomookun.arch4j.core.notification.entity.NotificationEntity;
import org.chomookun.arch4j.core.notification.model.Notification;
import org.chomookun.arch4j.core.notification.model.Notifier;
import org.chomookun.arch4j.core.notification.repository.NotificationRepository;
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
public class NotificationConsumer {

    private final BlockingQueue<Notification> queue = new LinkedBlockingQueue<>();

    private final NotifierService notifierService;

    private final NotificationRepository notificationRepository;

    private final PlatformTransactionManager transactionManager;

    /**
     * Initializes the consumer thread
     */
    @PostConstruct
    public void init() {
        Thread thread = new Thread(this::consumeNotification, this.getClass().getSimpleName());
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Adds notification to queue
     * @param notification notification
     */
    public void addNotification(Notification notification) {
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .notificationId(notification.getNotificationId())
                .notifierId(notification.getNotifierId())
                .notifierName(notification.getNotifierName())
                .to(notification.getTo())
                .subject(notification.getSubject())
                .content(notification.getContent())
                .submittedAt(Instant.now())
                .status(Notification.Status.SUBMITTED)
                .build();
        try {
            queue.add(notification);
        } catch (Throwable t) {
            log.warn(t.getMessage());
            notificationEntity.setStatus(Notification.Status.FAILED);
            notificationEntity.setErrorMessage(t.getMessage());
        } finally {
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager, def);
            transactionTemplate.executeWithoutResult(status -> {
                notificationRepository.saveAndFlush(notificationEntity);
            });
        }
    }

    /**
     * Consume notification
     */
    public void consumeNotification() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Notification notification = queue.take();
                notification.setSentAt(Instant.now());
                try {
                    Notifier notifier = notifierService.getNotifier(notification.getNotifierId()).orElseThrow();
                    NotifierClient notifierClient = NotifierClientFactory.getNotificationClient(notifier);
                    notifierClient.sendMessage(notification.getTo(), notification.getSubject(), notification.getContent(), notification.getOption());
                    notification.setStatus(Notification.Status.COMPLETED);
                } catch (Throwable t) {
                    log.warn(t.getMessage());
                    notification.setStatus(Notification.Status.FAILED);
                    notification.setErrorMessage(t.getMessage());
                    throw t;
                } finally {
                    DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                    def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
                    TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager, def);
                    transactionTemplate.executeWithoutResult(status -> {
                        notificationRepository.updateResult(
                                notification.getNotificationId(),
                                notification.getSentAt(),
                                notification.getStatus(),
                                notification.getErrorMessage());
                    });
                }
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
