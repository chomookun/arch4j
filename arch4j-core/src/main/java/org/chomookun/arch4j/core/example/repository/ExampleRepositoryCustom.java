package org.chomookun.arch4j.core.example.repository;

import org.chomookun.arch4j.core.example.entity.ExampleEntity;
import org.chomookun.arch4j.core.example.model.ExampleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ExampleRepositoryCustom {

    Page<ExampleEntity> findAllWithQueryDsl(ExampleSearch exampleSearch, Pageable pageable);

}
