package org.chomookun.arch4j.core.example;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.RowBounds;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.common.data.PageableUtils;
import org.chomookun.arch4j.core.example.entity.ExampleEntity;
import org.chomookun.arch4j.core.example.entity.ExampleItemEntity;
import org.chomookun.arch4j.core.example.mapper.ExampleMapper;
import org.chomookun.arch4j.core.example.model.Example;
import org.chomookun.arch4j.core.example.model.ExampleItem;
import org.chomookun.arch4j.core.example.model.ExampleSearch;
import org.chomookun.arch4j.core.example.repository.ExampleRepository;
import org.chomookun.arch4j.core.example.vo.ExampleVo;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExampleService {

    private final ExampleRepository exampleRepository;

    private final ExampleMapper exampleMapper;

    private final ModelMapper modelMapper;

    /**
     * Gets examples
     * @param exampleSearch example search
     * @param pageable pageable
     * @return page of examples
     */
    public Page<Example> getExamples(ExampleSearch exampleSearch, Pageable pageable) {
        Page<ExampleEntity> exampleEntityPage = exampleRepository.findAll(exampleSearch, pageable);
        List<Example> examples = exampleEntityPage.getContent().stream()
                .map(Example::from)
                .toList();
        long total = exampleEntityPage.getTotalElements();
        return new PageImpl<>(examples, pageable, total);
    }

    /**
     * Gets specified example
     * @param exampleId example id
     * @return specified example
     */
    public Optional<Example> getExample(String exampleId) {
        return exampleRepository.findById(exampleId)
                .map(Example::from);
    }

    /**
     * Saves example
     * @param example example
     * @return saved example
     */
    @Transactional
    public Example saveExample(Example example) {
        ExampleEntity exampleEntity;
        if (example.getExampleId() != null) {
            exampleEntity = exampleRepository.findById(example.getExampleId()).orElseThrow();
        } else {
            exampleEntity = ExampleEntity.builder()
                    .exampleId(IdGenerator.uuid())
                    .build();
        }
        exampleEntity.setName(example.getName());
        exampleEntity.setIcon(example.getIcon());
        exampleEntity.setNumber(example.getNumber());
        exampleEntity.setDecimalNumber(example.getDecimalNumber());
        exampleEntity.setDateTime(example.getDateTime());
        exampleEntity.setInstantDateTime(example.getInstantDateTime());
        exampleEntity.setZonedDateTime(example.getZonedDateTime());
        exampleEntity.setDate(example.getDate());
        exampleEntity.setTime(example.getTime());
        exampleEntity.setEnabled(example.isEnabled());
        exampleEntity.setType(example.getType());
        exampleEntity.setCode(example.getCode());
        exampleEntity.setText(example.getText());
        exampleEntity.setCryptoText(example.getCryptoText());
        // example items
        exampleEntity.getExampleItems().clear();
        for (int i = 0; i < example.getExampleItems().size(); i++) {
            ExampleItem exampleItem = example.getExampleItems().get(i);
            ExampleItemEntity exampleItemEntity = ExampleItemEntity.builder()
                    .exampleId(exampleEntity.getExampleId())
                    .itemId(exampleItem.getItemId())
                    .sort(i)
                    .name(exampleItem.getName())
                    .build();
            exampleEntity.getExampleItems().add(exampleItemEntity);
        }
        ExampleEntity savedExampleEntity = exampleRepository.saveAndFlush(exampleEntity);
        return Example.from(savedExampleEntity);
    }

    /**
     * Deletes specified example
     * @param exampleId example id
     */
    @Transactional
    public void deleteExample(String exampleId) {
        exampleRepository.deleteById(exampleId);
    }


}
