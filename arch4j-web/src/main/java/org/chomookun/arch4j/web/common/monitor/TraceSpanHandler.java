package org.chomookun.arch4j.web.common.monitor;

import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;
import org.springframework.stereotype.Component;

@Component
public class TraceSpanHandler extends SpanHandler {

    @Override
    public boolean end(TraceContext context, MutableSpan span, Cause cause) {
        // 예: 로그 출력
        System.out.println("[Span] traceId=" + context.traceIdString() +
                ", name=" + span.name() +
                ", duration=" + span.finishTimestamp() +
                ", tags=" + span.tags());

        String httpPath = span.tag("http.path");
        if (httpPath != null) {
            // 예: httpPath를 사용하여 추가적인 처리를 수행
            System.out.println("HTTP Path: " + httpPath);
        }

        // TODO: DB 저장, Kafka 전송 등 커스텀 처리 가능
        return true; // true를 반환하면 다음 handler로 전달
    }

}
