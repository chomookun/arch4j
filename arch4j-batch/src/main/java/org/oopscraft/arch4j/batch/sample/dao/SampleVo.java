package org.oopscraft.arch4j.batch.sample.dao;

import lombok.*;
import org.oopscraft.arch4j.core.data.crpyto.Crypto;
import org.oopscraft.arch4j.core.sample.SampleType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleVo {

    private String sampleId;

    private String sampleName;

    private SampleType sampleType;

    private Integer number;

    private Long longNumber;

    private Double doubleNumber;

    private BigDecimal bigDecimal;

    private LocalDateTime localDateTime;

    private LocalDate localDate;

    private LocalTime localTime;

    private String lobText;

    @Crypto
    private String cryptoText;

}
