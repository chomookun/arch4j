package org.chomookun.arch4j.web.api.v1.example.dto;

import lombok.Builder;
import lombok.Getter;
import org.chomookun.arch4j.core.example.model.Example;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class ExampleResponse {

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

    @Builder.Default
    private List<ExampleItemResponse> exampleItems = new ArrayList<>();

    /**
     * Factory method from model
     * @param example example
     * @return example response
     */
    public static ExampleResponse from(Example example) {
        return ExampleResponse.builder()
                .exampleId(example.getExampleId())
                .name(example.getName())
                .icon(example.getIcon())
                .number(example.getNumber())
                .decimalNumber(example.getDecimalNumber())
                .dateTime(example.getDateTime())
                .instantDateTime(example.getInstantDateTime())
                .zonedDateTime(example.getZonedDateTime())
                .date(example.getDate())
                .time(example.getTime())
                .enabled(example.isEnabled())
                .type(example.getType())
                .code(example.getCode())
                .text(example.getText())
                .cryptoText(example.getCryptoText())
                .exampleItems(example.getExampleItems().stream()
                        .map(ExampleItemResponse::from).toList())
                .build();
    }
}
