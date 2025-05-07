package org.chomookun.arch4j.core.template;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.message.MessageSource;
import org.chomookun.arch4j.core.template.entity.TemplateEntity;
import org.chomookun.arch4j.core.template.model.Template;
import org.chomookun.arch4j.core.template.model.TemplateSearch;
import org.chomookun.arch4j.core.template.repository.TemplateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplateService  {

    @PersistenceContext
    private EntityManager entityManager;

    private final TemplateRepository templateRepository;

    private final MessageSource messageSource;

    @Transactional
    public Template saveTemplate(Template template) {
        TemplateEntity templateEntity = Optional.ofNullable(template.getTemplateId())
                .flatMap(templateRepository::findById)
                .orElse(TemplateEntity.builder()
                    .templateId(template.getTemplateId())
                    .build());
        templateEntity.setName(template.getName());
        templateEntity.setFormat(template.getFormat());
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

    public void renderTemplate(Template template, Map<String,Object> variables) {
        // template engin
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateEngineMessageSource(messageSource);
        engine.setMessageSource(messageSource);
        engine.setEnableSpringELCompiler(true);
        // template resolver
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(template.getFormat().name());
        templateResolver.setCacheable(false);
        templateResolver.setCheckExistence(false);
        engine.setTemplateResolver(templateResolver);
        // context
        Context context = new Context();
        variables.forEach(context::setVariable);
        // render
        String subject = engine.process(template.getSubject(), context);
        String content = engine.process(template.getContent(), context);
        template.setSubject(subject);
        template.setContent(content);
    }

}
