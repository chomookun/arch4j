package org.chomookun.arch4j.core.template.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TemplateSearch {

    private String templateId;

    private String name;

}
