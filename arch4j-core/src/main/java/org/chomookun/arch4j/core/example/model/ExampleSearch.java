package org.chomookun.arch4j.core.example.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
public class ExampleSearch {

    private String exampleId;

    private String name;

    private Example.Type type;

    private String code;

    private String daoType;

}
