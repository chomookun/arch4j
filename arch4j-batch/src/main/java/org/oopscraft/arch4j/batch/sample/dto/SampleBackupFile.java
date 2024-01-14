package org.oopscraft.arch4j.batch.sample.dto;

import lombok.*;
import org.oopscraft.arch4j.batch.item.file.annotation.Align;
import org.oopscraft.arch4j.batch.item.file.annotation.Length;
import org.oopscraft.arch4j.core.data.crpyto.Crypto;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleBackupFile {

    @Length(50)
    private String sampleId;

    @Length(100)
    private String name;

    @Length(20)
    private String type;

    @Length(value = 10, align = Align.RIGHT)
    private Integer number;

    @Length(value = 10, align = Align.RIGHT)
    private Long longNumber;

    @Length(value = 10, align = Align.RIGHT)
    private Double doubleNumber;

    @Length(value = 10, align = Align.RIGHT)
    private BigDecimal bigDecimal;

    @Length(20)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime localDateTime;

    @Length(20)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate localDate;

    @Length(20)
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime localTime;

    @Length(100)
    private String lobText;

    @Length(100)
    private String cryptoText;

}
