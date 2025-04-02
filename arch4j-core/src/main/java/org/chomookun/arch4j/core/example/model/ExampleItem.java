package org.chomookun.arch4j.core.example.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseModel;
import org.chomookun.arch4j.core.example.entity.ExampleItemEntity;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExampleItem extends BaseModel {

    private String exampleId;

    private String itemId;

    private Integer sort;

    private String name;

    /**
     * Convert from ExampleItemEntity to ExampleItem
     * @param exampleItemEntity example item entity
     * @return example item
     */
    public static ExampleItem from(ExampleItemEntity exampleItemEntity) {
        return ExampleItem.builder()
                .exampleId(exampleItemEntity.getExampleId())
                .itemId(exampleItemEntity.getItemId())
                .sort(exampleItemEntity.getSort())
                .name(exampleItemEntity.getName())
                .build();
    }

}
