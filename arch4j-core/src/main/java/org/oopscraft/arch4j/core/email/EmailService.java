package org.oopscraft.arch4j.core.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.core.email.dao.EmailTemplateEntity;
import org.oopscraft.arch4j.core.email.dao.EmailTemplateRepository;
import org.oopscraft.arch4j.core.email.dao.EmailVerificationEntity;
import org.oopscraft.arch4j.core.email.dao.EmailVerificationRepository;
import org.oopscraft.arch4j.core.message.MessageSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements InitializingBean {

    private final EmailTemplateRepository emailTemplateRepository;

    private final EmailVerificationRepository emailVerificationRepository;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private SpringTemplateEngine templateEngine;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Override
    public void afterPropertiesSet() throws Exception {
        templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateEngineMessageSource(messageSource);
        templateEngine.setMessageSource(messageSource);
        templateEngine.setEnableSpringELCompiler(true);
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(templateResolver);
    }

    @Transactional
    public EmailTemplate saveEmailTemplate(EmailTemplate emailTemplate) {
        EmailTemplateEntity emailTemplateEntity = Optional.ofNullable(emailTemplate.getTemplateId())
                .flatMap(emailTemplateRepository::findById)
                .orElse(EmailTemplateEntity.builder()
                    .templateId(emailTemplate.getTemplateId())
                    .build());

        emailTemplateEntity.setSystemUpdatedAt(LocalDateTime.now());    // disable dirty checking
        emailTemplateEntity.setTemplateName(emailTemplate.getTemplateName());
        emailTemplateEntity.setSubject(emailTemplate.getSubject());
        emailTemplateEntity.setContent(emailTemplate.getContent());

        EmailTemplateEntity savedEmailTemplateEntity = emailTemplateRepository.saveAndFlush(emailTemplateEntity);
        return EmailTemplate.from(savedEmailTemplateEntity);
    }

    public Page<EmailTemplate> getEmailTemplates(EmailTemplateSearch emailTemplateSearch, Pageable pageable) {
        Page<EmailTemplateEntity> emailTemplatePage = emailTemplateRepository.findAll(emailTemplateSearch, pageable);
        List<EmailTemplate> emailTemplates = emailTemplatePage.getContent().stream()
                        .map(EmailTemplate::from)
                        .collect(Collectors.toList());
        return new PageImpl<>(emailTemplates, pageable, emailTemplatePage.getTotalElements());
    }

    public Optional<EmailTemplate> getEmailTemplate(String templateId) {
        return emailTemplateRepository.findById(templateId)
                .map(EmailTemplate::from);
    }

    @Transactional
    public void deleteEmailTemplate(String templateId) {
        emailTemplateRepository.deleteById(templateId);
        emailTemplateRepository.flush();
    }

    @Transactional
    public void sendEmailWidthTemplate(String to, EmailTemplate emailTemplate) throws EmailException {
        Context context = new Context();
        emailTemplate.getVariables().forEach(context::setVariable);
        String subject = templateEngine.process(emailTemplate.getSubject(), context);
        String content = templateEngine.process(emailTemplate.getContent(), context);
        sendEmail(to, subject, content);
    }

    @Transactional
    public void sendEmail(String to, String subject, String content) throws EmailException {
        executorService.submit(() -> {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
                mimeMessageHelper.setTo(to);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setText(content);
                javaMailSender.send(mimeMessage);
            } catch (MessagingException e) {
                log.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage(), e);
            }
        });
    }

    @Transactional
    public void issueEmailVerification(String email) {

        // answer
        SecureRandom random = new SecureRandom();
        String answer = String.valueOf(100000 + random.nextInt(89999));

        // send email
        EmailTemplate emailTemplate = getEmailTemplate("VERIFICATION").orElseThrow();
        emailTemplate.addVariable("answer", answer);
        try {
            sendEmailWidthTemplate(email, emailTemplate);
        } catch (Throwable t) {
            try {
                sendEmail(email, "Email Verification Answer", answer);
            } catch (EmailException e) {
                throw new RuntimeException(e);
            }
        }

        // save entity
        EmailVerificationEntity emailVerificationEntity = EmailVerificationEntity.builder()
                .email(email)
                .issuedAt(LocalDateTime.now())
                .answer(answer)
                .build();
        emailVerificationRepository.saveAndFlush(emailVerificationEntity);
    }

    public void validateEmailVerification(String email, String answer) {
        EmailVerificationEntity emailVerificationEntity = emailVerificationRepository.findById(email)
                .orElseThrow(() -> new RuntimeException("email verification request is not found."));
        if(!answer.contentEquals(emailVerificationEntity.getAnswer())) {
            throw new RuntimeException("answer is incorrect");
        }
        if(emailVerificationEntity.getIssuedAt().isBefore(LocalDateTime.now().minusMinutes(10))) {
            throw new RuntimeException("verification is expired");
        }
    }

}
