package org.chomookun.arch4j.core.example.vo;

import lombok.*;
import org.chomookun.arch4j.core.example.model.Example;

import java.math.BigDecimal;
import java.time.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExampleVo {

    private String exampleId;

    private String name;

    private String icon;

    private Integer number;

    private BigDecimal decimalNumber;

    private LocalDateTime dateTime;

    private Instant instantDateTime;

    private ZonedDateTime zonedDateTime;

    private LocalDate date;

    private LocalTime time;

    private boolean enabled;

    private Example.Type type;

    private String code;

    private String text;

    private String cryptoText;

}
