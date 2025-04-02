package org.chomookun.arch4j.core.example.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseModel;
import org.chomookun.arch4j.core.common.data.converter.BooleanConverter;
import org.chomookun.arch4j.core.common.data.converter.CryptoConverter;
import org.chomookun.arch4j.core.common.data.converter.GenericEnumConverter;
import org.chomookun.arch4j.core.example.model.Example;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "core_example_backup")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ExampleBackupEntity extends BaseModel {

    @Id
    @Column(name = "example_id", length = 32)
    private String exampleId;

    @Column(name = "name")
    private String name;

    @Column(name = "icon", length = Integer.MAX_VALUE)
    @Lob
    private String icon;

    @Column(name = "number")
    private Integer number;

    @Column(name = "decimal_number", precision = 19, scale = 4)
    private BigDecimal decimalNumber;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "instant_date_time")
    private Instant instantDateTime;

    @Column(name = "zoned_date_time")
    private ZonedDateTime zonedDateTime;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time")
    private LocalTime time;

    @Column(name = "enabled", length = 1)
    @Convert(converter = BooleanConverter.class)
    private boolean enabled;

    @Column(name = "type")
    private Example.Type type;

    @Column(name = "code")
    private String code;

    @Column(name = "text", length = 4000)
    @Lob
    private String text;

    @Column(name = "crypto_text", length = Integer.MAX_VALUE)
    @Lob
    @Convert(converter = CryptoConverter.class)
    private String cryptoText;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "example_id", updatable = false)
    @OrderBy("sort")
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private List<ExampleBackupItemEntity> exampleBackupItems = new ArrayList<>();

}
