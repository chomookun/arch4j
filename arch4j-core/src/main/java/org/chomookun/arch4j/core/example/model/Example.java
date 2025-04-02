package org.chomookun.arch4j.core.example.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseModel;
import org.chomookun.arch4j.core.example.entity.ExampleEntity;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Example extends BaseModel {

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

    private Type type;

    private String code;

    private String text;

    private String cryptoText;

    @Builder.Default
    private List<ExampleItem> exampleItems = new ArrayList<>();

    /**
     * Type enum
     */
    public enum Type {
        HUMAN, ANIMAL, FRUIT
    }

    /**
     * Factory method from entity
     * @param exampleEntity example entity
     * @return example model
     */
    public static Example from(ExampleEntity exampleEntity) {
        return Example.builder()
                .exampleId(exampleEntity.getExampleId())
                .name(exampleEntity.getName())
                .icon(exampleEntity.getIcon())
                .number(exampleEntity.getNumber())
                .decimalNumber(exampleEntity.getDecimalNumber())
                .dateTime(exampleEntity.getDateTime())
                .instantDateTime(exampleEntity.getInstantDateTime())
                .zonedDateTime(exampleEntity.getZonedDateTime())
                .date(exampleEntity.getDate())
                .time(exampleEntity.getTime())
                .enabled(exampleEntity.isEnabled())
                .type(exampleEntity.getType())
                .code(exampleEntity.getCode())
                .text(exampleEntity.getText())
                .cryptoText(exampleEntity.getCryptoText())
                .exampleItems(exampleEntity.getExampleItems().stream()
                        .map(ExampleItem::from).toList())
                .build();
    }

}
