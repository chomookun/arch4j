package org.chomookun.arch4j.core.example;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.example.entity.ExampleEntity;
import org.chomookun.arch4j.core.example.model.Example;
import org.chomookun.arch4j.core.example.model.ExampleSearch;
import org.chomookun.arch4j.core.example.repository.ExampleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Query dsl example service
 */
@Service
@RequiredArgsConstructor
public class QueryDslExampleService {

    private final ExampleRepository exampleRepository;

    /**
     * Gets examples
     * @param exampleSearch example search
     * @param pageable pageable
     * @return page of examples
     */
    public Page<Example> getExamples(ExampleSearch exampleSearch, Pageable pageable) {
        Page<ExampleEntity> exampleEntityPage = exampleRepository.findAllWithQueryDsl(exampleSearch, pageable);
        List<Example> examples = exampleEntityPage.getContent().stream()
                .map(Example::from)
                .toList();
        long total = exampleEntityPage.getTotalElements();
        return new PageImpl<>(examples, pageable, total);
    }

}
