package org.chomoo.arch4j.core.sample.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SampleSearch {

    private String sampleId;

    private String sampleName;

    private SampleType sampleType;

}