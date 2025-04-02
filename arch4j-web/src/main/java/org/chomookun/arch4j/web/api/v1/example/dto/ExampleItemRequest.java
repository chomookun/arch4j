package org.chomookun.arch4j.web.api.v1.example.dto;

import lombok.*;
import org.chomookun.arch4j.core.example.model.ExampleItem;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class ExampleItemRequest {

    private String exampleId;

    private String itemId;

    private Integer sort;

    private String name;

}
