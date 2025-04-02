package org.chomookun.arch4j.core.example;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.common.data.PageableUtils;
import org.chomookun.arch4j.core.example.mapper.ExampleMapper;
import org.chomookun.arch4j.core.example.model.Example;
import org.chomookun.arch4j.core.example.model.ExampleItem;
import org.chomookun.arch4j.core.example.model.ExampleSearch;
import org.chomookun.arch4j.core.example.vo.ExampleItemVo;
import org.chomookun.arch4j.core.example.vo.ExampleVo;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MybatisExampleService {

    private final ExampleMapper exampleMapper;

    private final ModelMapper modelMapper;

    /**
     * Gets examples
     * @param exampleSearch example search
     * @param pageable pageable
     * @return page of examples
     */
    public Page<Example> getExamples(ExampleSearch exampleSearch, Pageable pageable) {
        List<Example> examples = exampleMapper.selectExamples(exampleSearch, PageableUtils.toRowBounds(pageable)).stream()
                .map(it -> modelMapper.map(it, Example.class))
                .toList();
        for (Example example : examples) {
            List<ExampleItem> exampleItems = exampleMapper.selectExampleItems(example.getExampleId()).stream()
                    .map(it -> modelMapper.map(it, ExampleItem.class))
                    .toList();
            example.setExampleItems(exampleItems);
        }
        long total = exampleMapper.selectExamplesCount(exampleSearch);
        return new PageImpl<>(examples, pageable, total);
    }

    /**
     * Gets specified example by mybatis
     * @param exampleId example id
     * @return specified example
     */
    public Optional<Example> getExample(String exampleId) {
        Example example = null;
        ExampleVo exampleVo = exampleMapper.selectExample(exampleId);
        List<ExampleItemVo> exampleItemVos = exampleMapper.selectExampleItems(exampleId);
        if (exampleVo != null) {
            example = modelMapper.map(exampleVo, Example.class);
            List<ExampleItem> exampleItems = exampleItemVos.stream()
                    .map(it -> modelMapper.map(it, ExampleItem.class))
                    .toList();
            example.setExampleItems(exampleItems);
        }
        return Optional.ofNullable(example);
    }

    /**
     * Saves example
     * @param example example
     * @return saved example
     */
    @Transactional
    public Example saveExample(Example example) {
        ExampleVo exampleVo;
        if (example.getExampleId() != null) {
            exampleVo = Optional.ofNullable(exampleMapper.selectExample(example.getExampleId())).orElseThrow();
        } else {
            exampleVo = ExampleVo.builder()
                    .exampleId(IdGenerator.uuid())
                    .build();
        }
        exampleVo.setName(example.getName());
        exampleVo.setIcon(example.getIcon());
        exampleVo.setNumber(example.getNumber());
        exampleVo.setDecimalNumber(example.getDecimalNumber());
        exampleVo.setDateTime(example.getDateTime());
        exampleVo.setInstantDateTime(example.getInstantDateTime());
        exampleVo.setZonedDateTime(example.getZonedDateTime());
        exampleVo.setDate(example.getDate());
        exampleVo.setTime(example.getTime());
        exampleVo.setEnabled(example.isEnabled());
        exampleVo.setType(example.getType());
        exampleVo.setCode(example.getCode());
        exampleVo.setText(example.getText());
        exampleVo.setCryptoText(example.getCryptoText());
        // example items
        List<ExampleItemVo> exampleItemVos = new ArrayList<>();
        for (int i = 0; i < example.getExampleItems().size(); i ++ ) {
            ExampleItem exampleItem = example.getExampleItems().get(i);
            ExampleItemVo exampleItemVo = ExampleItemVo.builder()
                    .exampleId(exampleVo.getExampleId())
                    .itemId(exampleItem.getItemId())
                    .sort(i)
                    .name(exampleItem.getName())
                    .build();
            exampleItemVos.add(exampleItemVo);
        }
        // upsert
        if (example.getExampleId() != null) {
            exampleMapper.updateExample(exampleVo);
            exampleMapper.deleteExampleItems(exampleVo.getExampleId());
            for (ExampleItemVo exampleItemVo : exampleItemVos) {
                exampleMapper.insertExampleItem(exampleItemVo);
            }
        } else {
            exampleMapper.insertExample(exampleVo);
            for (ExampleItemVo exampleItemVo : exampleItemVos) {
                exampleMapper.insertExampleItem(exampleItemVo);
            }
        }
        return this.getExample(exampleVo.getExampleId()).orElseThrow();
    }

    /**
     * Deletes specified example
     * @param exampleId example id
     */
    @Transactional
    public void deleteExample(String exampleId) {
        exampleMapper.deleteExampleItems(exampleId);
        exampleMapper.deleteExample(exampleId);
    }


}
