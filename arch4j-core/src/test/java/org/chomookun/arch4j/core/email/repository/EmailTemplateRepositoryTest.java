package org.chomookun.arch4j.core.email.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.email.entity.EmailEntity;
import org.chomookun.arch4j.core.email.model.Email;
import org.chomookun.arch4j.core.email.model.EmailSearch;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@Slf4j
class EmailTemplateRepositoryTest extends CoreTestSupport {

    private final EmailRepository emailTemplateRepository;

    @Test
    void findAll() {
        // given
        EmailSearch emailSearch = EmailSearch.builder()
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        // when
        Page<EmailEntity> emailEntityPage = emailTemplateRepository.findAll(emailSearch, pageable);
        // then
        log.info("emailEntityPage: {}", emailEntityPage);
    }

    @Test
    void findAllWithUnPaged() {
        // given
        EmailSearch emailSearch = EmailSearch.builder()
                .build();
        Pageable pageable = Pageable.unpaged();
        // when
        Page<EmailEntity> emailEntityPage = emailTemplateRepository.findAll(emailSearch, pageable);
        // then
        log.info("emailEntityPage: {}", emailEntityPage);
    }

}