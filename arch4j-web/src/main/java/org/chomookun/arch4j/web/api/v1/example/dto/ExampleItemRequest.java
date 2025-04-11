package org.chomookun.arch4j.web.api.v1.example.dto;

import lombok.*;

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
