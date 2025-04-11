package org.chomookun.arch4j.web.api.v1.page.dto;

import lombok.*;
import org.chomookun.arch4j.core.page.model.Page;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageResponse {

    private String pageId;

    private String name;

    private Page.ContentFormat contentFormat;

    private String content;

    public static PageResponse from(Page page) {
        return PageResponse.builder()
                .pageId(page.getPageId())
                .name(page.getName())
                .contentFormat(page.getContentFormat())
                .content(page.getContent())
                .build();
    }

}
