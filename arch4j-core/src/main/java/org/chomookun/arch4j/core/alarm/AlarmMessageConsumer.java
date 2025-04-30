package org.chomookun.arch4j.core.alarm;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.alarm.client.AlarmClient;
import org.chomookun.arch4j.core.alarm.client.AlarmClientFactory;
import org.chomookun.arch4j.core.alarm.entity.AlarmMessageEntity;
import org.chomookun.arch4j.core.alarm.model.Alarm;
import org.chomookun.arch4j.core.alarm.model.AlarmMessage;
import org.chomookun.arch4j.core.alarm.repository.AlarmMessageRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@Lazy(false)
@RequiredArgsConstructor
@Slf4j
public class AlarmMessageConsumer {

    private final BlockingQueue<AlarmMessage> queue = new LinkedBlockingQueue<>();

    private final AlarmService alarmService;

    private final AlarmMessageRepository alarmMessageRepository;

    private final PlatformTransactionManager transactionManager;

    /**
     * Initializes the consumer thread
     */
    @PostConstruct
    public void init() {
        Thread thread = new Thread(this::consumeAlarmMessage, this.getClass().getSimpleName());
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Adds alarm message to queue
     * @param alarmMessage alarm message
     */
    public void addAlarmMessage(AlarmMessage alarmMessage) {
        AlarmMessageEntity alarmMessageEntity = AlarmMessageEntity.builder()
                .messageId(alarmMessage.getMessageId())
                .alarmId(alarmMessage.getAlarmId())
                .alarmName(alarmMessage.getAlarmName())
                .subject(alarmMessage.getSubject())
                .content(alarmMessage.getContent())
                .submittedAt(Instant.now())
                .status(AlarmMessage.Status.SUBMITTED)
                .build();
        try {
            queue.add(alarmMessage);
        } catch (Throwable t) {
            log.warn(t.getMessage());
            alarmMessageEntity.setStatus(AlarmMessage.Status.FAILED);
            alarmMessageEntity.setErrorMessage(t.getMessage());
        } finally {
            alarmMessageRepository.saveAndFlush(alarmMessageEntity);
        }
    }

    /**
     * Consume alarm message
     */
    public void consumeAlarmMessage() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                AlarmMessage alarmMessage = queue.take();
                alarmMessage.setSentAt(Instant.now());
                try {
                    Alarm alarm = alarmService.getAlarm(alarmMessage.getAlarmId()).orElseThrow();
                    AlarmClient alarmClient = AlarmClientFactory.getAlarmClient(alarm);
                    alarmClient.sendMessage(alarmMessage.getSubject(), alarmMessage.getContent());
                    alarmMessage.setStatus(AlarmMessage.Status.COMPLETED);
                } catch (Throwable t) {
                    log.warn(t.getMessage());
                    alarmMessage.setStatus(AlarmMessage.Status.FAILED);
                    alarmMessage.setErrorMessage(t.getMessage());
                } finally {
                    TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
                    transactionTemplate.executeWithoutResult(status -> {
                        alarmMessageRepository.updateResult(
                                alarmMessage.getAlarmId(),
                                alarmMessage.getMessageId(),
                                alarmMessage.getSentAt(),
                                alarmMessage.getStatus(),
                                alarmMessage.getErrorMessage());
                    });
                }
            } catch (Throwable e) {
                log.warn(e.getMessage());
            }
        }
    }

}
