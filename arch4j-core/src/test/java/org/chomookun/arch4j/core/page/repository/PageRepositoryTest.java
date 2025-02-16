package org.chomookun.arch4j.core.page.repository;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.page.entity.PageEntity;
import org.chomookun.arch4j.core.page.entity.PageWidgetEntity;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class PageRepositoryTest extends CoreTestSupport {

    private final PageRepository pageRepository;

    private PageEntity getTestPageEntity() {
        PageEntity pageEntity = PageEntity.builder()
                .pageId("test-page")
                .name("test page")
                .build();
        IntStream.range(0,3).forEach(index -> {
            pageEntity.getPageWidgets().add(PageWidgetEntity.builder()
                    .pageId(pageEntity.getPageId())
                    .index(index)
                    .type(this.getClass().getName())
                    .properties("key=value")
                    .build());
        });
        return pageEntity;
    }

    private PageEntity saveTestPageEntity() {
        PageEntity pageEntity = getTestPageEntity();
        entityManager.persist(pageEntity);
        entityManager.flush();
        entityManager.clear();
        return pageEntity;
    }

    @Test
    @Order(1)
    void saveToPersist() {
        // given
        PageEntity testPageEntity = getTestPageEntity();

        // when
        pageRepository.saveAndFlush(testPageEntity);

        // then
        entityManager.clear();
        assertNotNull(entityManager.find(PageEntity.class, testPageEntity.getPageId()));
    }

    @Test
    @Order(2)
    void saveToMerge() {
        // given
        PageEntity testPageEntity = saveTestPageEntity();

        // then
        testPageEntity.setName("changed");
        PageEntity pageEntity = pageRepository.saveAndFlush(testPageEntity);

        // then
        entityManager.clear();
        assertEquals(
                "changed",
                entityManager.find(PageEntity.class, pageEntity.getPageId()).getName()
        );
    }

    @Test
    @Order(3)
    void findById() {
        // given
        PageEntity testPageEntity = saveTestPageEntity();

        // when
        PageEntity pageEntity = pageRepository.findById(testPageEntity.getPageId())
                .orElseThrow();

        // then
        assertEquals(testPageEntity.getPageId(), pageEntity.getPageId());
    }

    @Test
    @Order(4)
    void deleteById() {
        // given
        PageEntity testPageEntity = saveTestPageEntity();

        // when
        pageRepository.deleteById(testPageEntity.getPageId());
        pageRepository.flush();

        // then
        entityManager.clear();
        assertNull(entityManager.find(PageEntity.class, testPageEntity.getPageId()));
    }

}