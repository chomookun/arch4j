package org.chomookun.arch4j.core.batch.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.batch.core.BatchStatus;

@Builder
@Getter
public class JobSearch {

    private String jobName;

    private BatchStatus lastBatchStatus;

}
