package org.chomookun.arch4j.core.message;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.message.model.MessageSearch;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.message.entity.MessageEntity;
import org.chomookun.arch4j.core.message.model.Message;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class MessageServiceTest extends CoreTestSupport {

    private final MessageService messageService;

    @Test
    void saveMessageForPersist() {
        // given
        Message message = Message.builder()
                .messageId("test.name")
                .name("test message")
                .build();
        // when
        Message savedMessage = messageService.saveMessage(message);
        // then
        assertNotNull(entityManager.find(MessageEntity.class, savedMessage.getMessageId()));
    }

    @Test
    void saveMessageForMerge() {
        // given
        MessageEntity messageEntity = MessageEntity.builder()
                .messageId("test.name")
                .name("test message")
                .build();
        entityManager.persist(messageEntity);
        entityManager.flush();
        // when
        entityManager.refresh(messageEntity);
        Message message = Message.from(messageEntity);
        message.setName("changed");
        Message savedMessage = messageService.saveMessage(message);
        // then
        assertEquals("changed", entityManager.find(MessageEntity.class, savedMessage.getMessageId()).getName());
    }

    @Test
    void getMenu() {
        // given
        MessageEntity messageEntity = MessageEntity.builder()
                .messageId("test.name")
                .name("test message")
                .build();
        entityManager.persist(messageEntity);
        entityManager.flush();
        // when
        Message message = messageService.getMessage(messageEntity.getMessageId()).orElse(null);
        // then
        assertNotNull(message);
    }

    @Test
    void deleteMessage() {
        // given
        MessageEntity messageEntity = MessageEntity.builder()
                .messageId("test.name")
                .name("test message")
                .build();
        entityManager.persist(messageEntity);
        entityManager.flush();
        // when
        messageService.deleteMessage(messageEntity.getMessageId());
        // then
        assertNull(entityManager.find(MessageEntity.class, messageEntity.getMessageId()));
    }

    @Test
    void getMessages() {
        // given
        MessageEntity messageEntity = MessageEntity.builder()
                .messageId("test.name")
                .name("test message")
                .build();
        entityManager.persist(messageEntity);
        entityManager.flush();
        MessageSearch messageSearch = MessageSearch.builder()
                .messageId("test.name")
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        // when
        List<Message> messages = messageService.getMessages(messageSearch, pageable).getContent();
        // then
        assertTrue(messages.stream().allMatch(e -> e.getMessageId().contains(messageSearch.getMessageId())));
    }

}