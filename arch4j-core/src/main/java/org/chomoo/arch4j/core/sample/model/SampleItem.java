package org.chomoo.arch4j.core.sample.model;

import lombok.*;
import org.chomoo.arch4j.core.sample.dao.SampleItemEntity;
import org.chomoo.arch4j.core.common.support.ModelMapperFactory;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleItem {

    private String sampleId;

    private String itemId;

    private String itemName;

    private String value;

    private Integer order;

    public static SampleItem from(SampleItemEntity sampleItemEntity) {
        return ModelMapperFactory.getInstance()
                .map(sampleItemEntity, SampleItem.class);
    }

}