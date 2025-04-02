package org.chomookun.arch4j.batch.example.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExampleFile {

    private String exampleId;

    private String name;

    private String icon;

    private String number;

    private String decimalNumber;

    private String dateTime;

    private String instantDateTime;

    private String zonedDateTime;

    private String date;

    private String time;

    private String enabled;

    private String type;

    private String code;

    private String text;

    private String cryptoText;

}
