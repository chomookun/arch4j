package org.chomookun.arch4j.core.template;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.email.entity.EmailEntity;
import org.chomookun.arch4j.core.email.entity.EmailVerificationEntity;
import org.chomookun.arch4j.core.email.exception.EmailException;
import org.chomookun.arch4j.core.email.model.Email;
import org.chomookun.arch4j.core.email.model.EmailSearch;
import org.chomookun.arch4j.core.email.repository.EmailVerificationRepository;
import org.chomookun.arch4j.core.message.MessageSource;
import org.chomookun.arch4j.core.template.entity.TemplateEntity;
import org.chomookun.arch4j.core.template.model.Template;
import org.chomookun.arch4j.core.template.model.TemplateSearch;
import org.chomookun.arch4j.core.template.repository.TemplateRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateService implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    private final TemplateRepository templateRepository;

//    private final EmailVerificationRepository emailVerificationRepository;

//    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private SpringTemplateEngine templateEngine;

//    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);

    /**
     * Initialize template engine
     * @throws Exception exception
     */
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
    public Template saveTemplate(Template template) {
        TemplateEntity templateEntity = Optional.ofNullable(template.getTemplateId())
                .flatMap(templateRepository::findById)
                .orElse(TemplateEntity.builder()
                    .templateId(template.getTemplateId())
                    .build());
        templateEntity.setName(template.getName());
        templateEntity.setSubject(template.getSubject());
        templateEntity.setContent(template.getContent());
        TemplateEntity savedTemplateEntity = templateRepository.saveAndFlush(templateEntity);
        entityManager.refresh(savedTemplateEntity);
        return Template.from(savedTemplateEntity);
    }

    public Optional<Template> getTemplate(String templateId) {
        return templateRepository.findById(templateId)
                .map(Template::from);
    }

    @Transactional
    public void deleteTemplate(String templateId) {
        templateRepository.deleteById(templateId);
        templateRepository.flush();
    }

    public Page<Template> getTemplates(TemplateSearch templateSearch, Pageable pageable) {
        Page<TemplateEntity> tempaltePage = templateRepository.findAll(templateSearch, pageable);
        List<Template> templates = tempaltePage.getContent().stream()
                .map(Template::from)
                .toList();
        long total = tempaltePage.getTotalElements();
        return new PageImpl<>(templates, pageable, total);
    }

//    @Transactional
//    public void sendEmailWidthTemplate(String to, Email emailTemplate) throws EmailException {
//        Context context = new Context();
//        emailTemplate.getVariables().forEach(context::setVariable);
//        String subject = templateEngine.process(emailTemplate.getSubject(), context);
//        String content = templateEngine.process(emailTemplate.getContent(), context);
//        sendEmail(to, subject, content);
//    }
//
//    /**
//     * Sends email
//     * @param to email address
//     * @param subject subject
//     * @param content content
//     */
//    @Transactional
//    public void sendEmail(String to, String subject, String content) throws EmailException {
//        executorService.submit(() -> {
//            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//            try {
//                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
//                mimeMessageHelper.setTo(to);
//                mimeMessageHelper.setSubject(subject);
//                mimeMessageHelper.setText(content);
//                javaMailSender.send(mimeMessage);
//            } catch (MessagingException e) {
//                log.error(e.getMessage(), e);
//                throw new RuntimeException(e.getMessage(), e);
//            }
//        });
//    }
//
//    /**
//     * Issues email verification
//     * @param email email address
//     */
//    @Transactional
//    public void issueEmailVerification(String email) {
//        // answer
//        SecureRandom random = new SecureRandom();
//        String answer = String.valueOf(100000 + random.nextInt(89999));
//        // send email
//        Email emailTemplate = getEmail("VERIFICATION").orElseThrow();
//        emailTemplate.addVariable("answer", answer);
//        try {
//            sendEmailWidthTemplate(email, emailTemplate);
//        } catch (Throwable t) {
//            try {
//                sendEmail(email, "Email Verification Answer", answer);
//            } catch (EmailException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        // save entity
//        EmailVerificationEntity emailVerificationEntity = EmailVerificationEntity.builder()
//                .email(email)
//                .issuedAt(LocalDateTime.now())
//                .answer(answer)
//                .build();
//        emailVerificationRepository.saveAndFlush(emailVerificationEntity);
//    }
//
//    /**
//     * Validates email verification
//     * @param email email address
//     * @param answer answer
//     */
//    public void validateEmailVerification(String email, String answer) {
//        EmailVerificationEntity emailVerificationEntity = emailVerificationRepository.findById(email)
//                .orElseThrow(() -> new RuntimeException("email verification request is not found."));
//        if(!answer.contentEquals(emailVerificationEntity.getAnswer())) {
//            throw new RuntimeException("answer is incorrect");
//        }
//        if(emailVerificationEntity.getIssuedAt().isBefore(LocalDateTime.now().minusMinutes(10))) {
//            throw new RuntimeException("verification is expired");
//        }
//    }

}
