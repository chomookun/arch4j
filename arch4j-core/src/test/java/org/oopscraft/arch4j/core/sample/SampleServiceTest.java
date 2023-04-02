package org.oopscraft.arch4j.core.sample;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.sample.repository.SampleEntity;
import org.oopscraft.arch4j.core.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class SampleServiceTest extends CoreTestSupport {

    final SampleService sampleService;

    Sample testSample = Sample.builder()
            .id(UUID.randomUUID().toString())
            .name("name")
            .bigDecimal(BigDecimal.valueOf(1234))
            .build();

    @Test
    @Order(1)
    void saveSample() {

        // save
        sampleService.saveSample(testSample);

        // check
        assertNotNull(entityManager.find(SampleEntity.class, testSample.getId()));
    }

    @Test
    @Order(2)
    void getSample() {

        // save
        saveSample();

        // get sample
        Sample sample = sampleService.getSample(testSample.getId()).orElse(null);

        // check
        assertNotNull(sample);
        assertEquals(testSample.getId(), sample.getId());
    }

    @Test
    @Order(3)
    public void deleteSample() {

        // save
        saveSample();

        // delete
        sampleService.deleteSample(testSample.getId());

        // check
        assertNull(entityManager.find(SampleEntity.class, testSample.getId()));
    }

    @Test
    @Order(4)
    public void getSamplesByJpa() {

        // save
        saveSample();

        // search
        SampleSearch sampleSearch = SampleSearch.builder()
                .name(testSample.getName())
                .build();
        Page<Sample> samplePage = sampleService.getSamplesByJpa(sampleSearch, PageRequest.of(0, 10));

        // check
        assertTrue(samplePage.getContent().size() > 0);
        assertTrue(samplePage.stream()
                .anyMatch(sample -> sample.getName().contains(sampleSearch.getName()))
        );
    }

    @Test
    @Order(5)
    public void getSamplesByQuerydsl() {

        // save
        saveSample();

        // search
        SampleSearch sampleSearch = SampleSearch.builder()
                .name(testSample.getName())
                .build();
        Page<Sample> samplePage = sampleService.getSamplesByQuerydsl(sampleSearch, PageRequest.of(0,10));

        // check
        assertTrue(samplePage.getContent().size() > 0);
        assertTrue(samplePage.getContent().stream()
                .allMatch(sample -> sample.getName().contains(sampleSearch.getName()))
        );
    }

    @Test
    @Order(6)
    void getSamplesByMybatis() {

        // save
        saveSample();

        // search
        SampleSearch sampleSearch = SampleSearch.builder()
                .name(testSample.getName())
                .build();
        Page<Sample> samplePage = sampleService.getSamplesByMybatis(sampleSearch, PageRequest.of(0, 10));

        // check
        assertTrue(samplePage.getContent().size() > 0);
        assertTrue(samplePage.getContent().stream()
                .allMatch(sample -> sample.getName().contains(sampleSearch.getName()))
        );
    }


}