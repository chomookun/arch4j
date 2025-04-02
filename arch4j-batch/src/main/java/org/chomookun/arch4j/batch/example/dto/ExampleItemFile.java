package org.chomookun.arch4j.batch.example.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExampleItemFile {

    private String exampleId;

    private String itemId;

    private String sort;

    private String name;

}