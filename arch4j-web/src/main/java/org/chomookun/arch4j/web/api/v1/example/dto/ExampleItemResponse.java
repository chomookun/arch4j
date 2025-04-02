package org.chomookun.arch4j.web.api.v1.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.chomookun.arch4j.core.example.model.ExampleItem;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@Getter
public class ExampleItemResponse {

    private String exampleId;

    @NotBlank(message = "itemId is is required.")
    private String itemId;

    private Integer sort;

    private String name;

    /**
     * Convert from ExampleItem to ExampleItemResponse
     * @param exampleItem example item
     * @return example item response
     */
    public static ExampleItemResponse from(ExampleItem exampleItem) {
        return ExampleItemResponse.builder()
                .exampleId(exampleItem.getExampleId())
                .itemId(exampleItem.getItemId())
                .sort(exampleItem.getSort())
                .name(exampleItem.getName())
                .build();
    }

}
