package org.chomookun.arch4j.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.chomookun.arch4j.core.execution.model.Execution;
import org.chomookun.arch4j.web.common.test.WebTestSupport;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
@Slf4j
public class WebConfigurationTest extends WebTestSupport {

    private final ObjectMapper objectMapper;

    @Builder
    @Getter
    static class TestObject {
        private LocalDate localDate;
        private LocalDateTime localDateTime;
        private Instant instant;
        private AtomicLong atomicLong;
        private Duration duration;
    }

    @Test
    void jackson2ObjectMapperBuilderCustomizerWithMap() throws Throwable {
        Map<String,Object> data = new HashMap<>();
        data.put("now", Instant.now());
        log.info("Instant.now():{}", objectMapper.writeValueAsString(Instant.now()));
        log.info("data:{}", objectMapper.writeValueAsString(data));
    }

    @Test
    void jackson2ObjectMapperBuilderCustomizerWithObject() throws Throwable {
        TestObject testObject = TestObject.builder()
                .localDate(LocalDate.now())
                .localDateTime(LocalDateTime.now())
                .instant(Instant.now())
                .atomicLong(new AtomicLong(100))
                .duration(Duration.ofSeconds(100))
                .build();
        log.info("testObject:{}", objectMapper.writeValueAsString(testObject));
    }

}
