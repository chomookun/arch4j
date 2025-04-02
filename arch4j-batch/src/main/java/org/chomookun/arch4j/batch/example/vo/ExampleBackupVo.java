package org.chomookun.arch4j.batch.example.vo;

import lombok.*;
import org.chomookun.arch4j.core.example.model.Example;

import java.math.BigDecimal;
import java.time.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExampleBackupVo {

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

    private String enabled;

    private String type;

    private String code;

    private String text;

    private String cryptoText;

}
