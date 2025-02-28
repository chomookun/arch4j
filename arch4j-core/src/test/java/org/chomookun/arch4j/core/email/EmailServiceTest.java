package org.chomookun.arch4j.core.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.email.entity.EmailEntity;
import org.chomookun.arch4j.core.email.exception.EmailException;
import org.chomookun.arch4j.core.email.model.Email;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@Slf4j
class EmailServiceTest extends CoreTestSupport {

    private final EmailService emailService;

    @Test
    void saveEmailForPersist() {
        Email email = Email.builder()
                .emailId("test")
                .name("test email")
                .subject("test subject")
                .content("test content")
                .build();
        Email savedEmail = emailService.saveEmail(email);
        assertNotNull(entityManager.find(EmailEntity.class, savedEmail.getEmailId()));
    }

    @Test
    void saveEmailForMerge() {
        // given
        EmailEntity emailEntity = EmailEntity.builder()
                .emailId("test")
                .name("test email")
                .subject("test subject")
                .content("test content")
                .build();
        entityManager.persist(emailEntity);
        entityManager.flush();
        entityManager.clear();
        // when
        Email email = Email.from(emailEntity);
        email.setName("changed");
        Email savedEmail = emailService.saveEmail(email);
        entityManager.clear();
        // then
        assertEquals("changed", entityManager.find(EmailEntity.class, savedEmail.getEmailId()).getName());
    }

    @Test
    void getEmail() {
        // given
        EmailEntity emailEntity = EmailEntity.builder()
                .emailId("test")
                .name("test email")
                .subject("test subject")
                .content("test content")
                .build();
        entityManager.persist(emailEntity);
        entityManager.flush();
        entityManager.clear();
        // when
        Email email = emailService.getEmail(emailEntity.getEmailId()).orElseThrow();
        // then
        assertNotNull(email);
    }

    @Test
    void deleteEmail() {
        // given
        EmailEntity emailEntity = EmailEntity.builder()
                .emailId("test")
                .name("test email")
                .subject("test subject")
                .content("test content")
                .build();
        entityManager.persist(emailEntity);
        entityManager.flush();
        entityManager.clear();
        // when
        emailService.deleteEmail(emailEntity.getEmailId());
        // then
        entityManager.clear();
        EmailEntity deletedEmailEntity = entityManager.find(EmailEntity.class, emailEntity.getEmailId());
        assertNull(deletedEmailEntity);
    }

    @Test
    public void sendEmail() {
        // given
        String to = "chomookun@gmail.com";
        String subject = "test email subject";
        String content = "test email content";
        // when
        try {
            emailService.sendEmail(to,subject,content);
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void sendEmailWithTemplate() {
        // given
        String to = "chomookun@gmail.com";
        EmailEntity testEmailTemplateEntity = EmailEntity.builder()
            .emailId("test_email")
            .name("test_name")
            .subject("subject ${subject}")
            .content("content ${content}")
            .build();
        entityManager.persist(testEmailTemplateEntity);
        entityManager.flush();
        // when
        Email emailTemplate = emailService.getEmail(testEmailTemplateEntity.getEmailId()).orElseThrow();
        emailTemplate.addVariable("subject", "test subject");
        emailTemplate.addVariable("content", "<b>test content</b> test content");
        try {
            emailService.sendEmailWidthTemplate(to, emailTemplate);
        }catch(EmailException e) {
            throw new RuntimeException(e);
        }
    }

}