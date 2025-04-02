package org.chomookun.arch4j.web.api.v1.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.chomookun.arch4j.core.example.model.Example;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class ExampleRequest {

    private String exampleId;

    @NotBlank(message = "name is required.")
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
    private List<ExampleItemRequest> exampleItems = new ArrayList<>();

}
