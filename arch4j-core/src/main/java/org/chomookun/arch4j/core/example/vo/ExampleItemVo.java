package org.chomookun.arch4j.core.example.vo;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExampleItemVo {

    private String exampleId;

    private String itemId;

    private Integer sort;

    private String name;

}
