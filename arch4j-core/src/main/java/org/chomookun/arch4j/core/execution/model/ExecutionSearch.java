package org.chomookun.arch4j.core.execution.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ExecutionSearch {

    private String taskName;

    private Execution.Status status;

}
