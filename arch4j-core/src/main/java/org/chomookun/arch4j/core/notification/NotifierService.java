package org.chomookun.arch4j.core.notification;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.pbe.PbePropertiesUtil;
import org.chomookun.arch4j.core.notification.client.NotifierClient;
import org.chomookun.arch4j.core.notification.client.NotifierClientFactory;
import org.chomookun.arch4j.core.notification.entity.NotifierEntity;
import org.chomookun.arch4j.core.notification.repository.NotifierRepository;
import org.chomookun.arch4j.core.notification.model.Notifier;
import org.chomookun.arch4j.core.notification.model.NotifierSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotifierService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final NotifierRepository notifierRepository;

    /**
     * Saves notification
     * @param notifier notification
     * @return saved notification
     */
    @Transactional
    public Notifier saveNotifier(Notifier notifier) {
        NotifierEntity notifierEntity = Optional.ofNullable(notifier.getNotifierId())
                .flatMap(notifierRepository::findById)
                .orElse(NotifierEntity.builder()
                        .notifierId(notifier.getNotifierId())
                        .build());
        notifierEntity.setSystemUpdatedAt(LocalDateTime.now());
        notifierEntity.setName(notifier.getName());
        notifierEntity.setClientType(notifier.getClientType());
        notifierEntity.setClientConfig(PbePropertiesUtil.encodePropertiesString(notifier.getClientConfig()));
        // saves
        NotifierEntity savedNotifierEntity = notifierRepository.saveAndFlush(notifierEntity);
        entityManager.refresh(savedNotifierEntity);
        return Notifier.from(savedNotifierEntity);
    }

    /**
     * Gets notification
     * @param notifierId notification id
     * @return notification
     */
    public Optional<Notifier> getNotifier(String notifierId) {
        return notifierRepository.findById(notifierId)
                .map(Notifier::from);
    }

    /**
     * Deletes alarm
     * @param notifierId alarm id
     */
    @Transactional
    public void deleteNotifier(String notifierId) {
        NotifierEntity notifierEntity = notifierRepository.findById(notifierId).orElseThrow();
        notifierRepository.delete(notifierEntity);
        notifierRepository.flush();
    }

    /**
     * Gets alarms
     * @param notifierSearch notification search
     * @param pageable pageable
     * @return page of notification
     */
    public Page<Notifier> getNotifiers(NotifierSearch notifierSearch, Pageable pageable) {
        Page<NotifierEntity> page = notifierRepository.findAll(notifierSearch, pageable);
        List<Notifier> notifiers = page.getContent().stream()
                .map(Notifier::from)
                .collect(Collectors.toList());
        return new PageImpl<>(notifiers, pageable, page.getTotalElements());
    }

    /**
     * Tests alarm
     * @param notifier alarm
     */
    public void testNotifier(Notifier notifier, String to, String subject, String content) {
        NotifierClient notifierClient = NotifierClientFactory.getNotificationClient(notifier);
        notifierClient.sendMessage(to, subject, content, null);
    }

}
