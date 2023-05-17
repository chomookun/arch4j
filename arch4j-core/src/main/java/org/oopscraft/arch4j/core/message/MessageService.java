package org.oopscraft.arch4j.core.message;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.message.repository.MessageEntity;
import org.oopscraft.arch4j.core.message.repository.MessageRepository;
import org.oopscraft.arch4j.core.message.repository.MessageSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    /**
     * save message
     * @param message message
     */
    public void saveMessage(Message message) {
        MessageEntity messageEntity = messageRepository.findById(message.getId()).orElse(null);
        if(messageEntity == null) {
            messageEntity = MessageEntity.builder()
                    .id(message.getId())
                    .build();
        }
        messageEntity.setName(message.getName());
        messageEntity.setValue(message.getValue());
        messageEntity.setNote(message.getNote());
        messageRepository.saveAndFlush(messageEntity);
    }

    /**
     * returns message
     * @param id message id
     * @return message
     */
    public Optional<Message> getMessage(String id) {
        return messageRepository.findById(id).map(Message::from);
    }

    /**
     * deletes message
     * @param id message id
     */
    public void deleteMessage(String id) {
        messageRepository.deleteById(id);
        messageRepository.flush();
    }

    /**
     * searches messages
     * @param messageSearch message search condition
     * @param pageable pagination info
     * @return message page
     */
    public Page<Message> getMessages(MessageSearch messageSearch, Pageable pageable) {

        // search condition
        Specification<MessageEntity> specification = (root, query, criteriaBuilder) -> null;
        if(messageSearch.getId() != null) {
            specification = specification.and(MessageSpecification.likeId(messageSearch.getId()));
        }
        if(messageSearch.getName() != null) {
            specification = specification.and(MessageSpecification.likeName(messageSearch.getName()));
        }

        // find data
        Page<MessageEntity> messageEntityPage = messageRepository.findAll(specification, pageable);
        List<Message> messages = messageEntityPage.getContent().stream()
                .map(Message::from)
                .collect(Collectors.toList());
        long total = messageEntityPage.getTotalElements();

        // returns
        return new PageImpl<>(messages, pageable, total);
    }




}
