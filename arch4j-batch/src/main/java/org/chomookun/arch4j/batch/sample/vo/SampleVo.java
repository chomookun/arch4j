package org.chomookun.arch4j.batch.sample.vo;

import lombok.*;
import org.chomookun.arch4j.core.common.crpyto.Crypto;
import org.chomookun.arch4j.core.sample.model.SampleType;

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
