package org.chomookun.arch4j.core.sample.repository;

import org.chomookun.arch4j.core.sample.entity.SampleEntity;
import org.chomookun.arch4j.core.sample.model.SampleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SampleRepositoryCustom {

    Page<SampleEntity> findSamples(SampleSearch sampleSearch, Pageable pageable);

}
