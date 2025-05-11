package org.chomookun.arch4j.web.common.monitor;

import java.time.Duration;
import java.time.Instant;

public class Trace {

    private String uri;

    private String method;

    private String traceId;

    private Instant start;

    private Instant end;

    private Status status;

    private String errorMessage;

    public Duration getElapsed() {
        return Duration.between(start, end);
    }

    public enum Status {
        SUCCESS,
        BAD_REQUEST,
        ERROR
    }

}
