package org.chomookun.arch4j.core.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.chomookun.arch4j.core.template.entity.TemplateEntity;
import org.chomookun.arch4j.core.template.model.Template;
import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import org.thymeleaf.templateresource.StringTemplateResource;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
@Slf4j
class TemplateServiceTest extends CoreTestSupport {

    private final TemplateService templateService;

    @Test
    void saveTemplateForPersist() {
        Template template = Template.builder()
                .templateId("test")
                .name("test template")
                .format(Template.Format.TEXT)
                .subject("test subject")
                .content("test content")
                .build();
        Template savedTemplate = templateService.saveTemplate(template);
        assertNotNull(entityManager.find(TemplateEntity.class, savedTemplate.getTemplateId()));
    }

    @Test
    void saveTemplateForMerge() {
        // given
        TemplateEntity templateEntity = TemplateEntity.builder()
                .templateId("test")
                .name("test template")
                .format(Template.Format.TEXT)
                .subject("test subject")
                .content("test content")
                .build();
        entityManager.persist(templateEntity);
        entityManager.flush();
        entityManager.clear();
        // when
        Template template = Template.from(templateEntity);
        template.setName("changed");
        Template savedTemplate = templateService.saveTemplate(template);
        entityManager.clear();
        // then
        assertEquals("changed", entityManager.find(TemplateEntity.class, savedTemplate.getTemplateId()).getName());
    }

    @Test
    void getTemplate() {
        // given
        TemplateEntity templateEntity = TemplateEntity.builder()
                .templateId("test")
                .name("test template")
                .format(Template.Format.TEXT)
                .subject("test subject")
                .content("test content")
                .build();
        entityManager.persist(templateEntity);
        entityManager.flush();
        entityManager.clear();
        // when
        Template template = templateService.getTemplate(templateEntity.getTemplateId()).orElseThrow();
        // then
        assertNotNull(template);
    }

    @Test
    void deleteTemplate() {
        // given
        TemplateEntity templateEntity = TemplateEntity.builder()
                .templateId("test")
                .name("test template")
                .format(Template.Format.TEXT)
                .subject("test subject")
                .content("test content")
                .build();
        entityManager.persist(templateEntity);
        entityManager.flush();
        entityManager.clear();
        // when
        templateService.deleteTemplate(templateEntity.getTemplateId());
        // then
        entityManager.clear();
        TemplateEntity deletedTemplateEntity = entityManager.find(TemplateEntity.class, templateEntity.getTemplateId());
        assertNull(deletedTemplateEntity);
    }

    @Test
    public void renderTemplateWithText() {
        // when
        Template template = Template.builder()
                .templateId("test_template")
                .name("test_name")
                .format(Template.Format.TEXT)
                .subject("subject [[${subject}]]")
                .content("content [[${content}]]")
                .build();
        Map<String,Object> variables = Map.of(
            "subject",  "test subject",
            "content", "test content"
        );
        templateService.renderTemplate(template, variables);
        // then
        log.info("subject: {}", template.getSubject());
        log.info("content: {}", template.getContent());
        assertTrue(template.getSubject().contains("test subject"));
        assertTrue(template.getContent().contains("test content"));
    }

    @Test
    public void renderTemplateWithHtml() {
        // when
        Template template = Template.builder()
                .templateId("test_template")
                .name("test_name")
                .format(Template.Format.HTML)
                .subject("<span data-th-text=\"${subject}\"></span>")
                .content("<span data-th-text=\"${content}\"></span>")
                .build();
        Map<String,Object> variables = Map.of(
                "subject",  "test subject",
                "content", "test content"
        );
        templateService.renderTemplate(template, variables);
        // then
        log.info("subject: {}", template.getSubject());
        log.info("content: {}", template.getContent());
        assertTrue(template.getSubject().contains("test subject"));
        assertTrue(template.getContent().contains("test content"));
    }

}