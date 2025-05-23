package org.chomookun.arch4j.web.api.v1.page.dto;

import lombok.*;
import org.chomookun.arch4j.core.page.model.Page;
import org.chomookun.arch4j.core.page.model.PageWidget;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageResponse {

    private String pageId;

    private String name;

    private String content;

    private Page.ContentFormat contentFormat;

    @Builder.Default
    private List<PageWidgetResponse> pageWidgets = new ArrayList<>();

    public static PageResponse from(Page page) {
        return PageResponse.builder()
                .pageId(page.getPageId())
                .name(page.getName())
                .content(page.getContent())
                .contentFormat(page.getContentFormat())
                .pageWidgets(page.getPageWidgets().stream()
                        .map(PageWidgetResponse::from)
                        .toList())
                .build();
    }

}
