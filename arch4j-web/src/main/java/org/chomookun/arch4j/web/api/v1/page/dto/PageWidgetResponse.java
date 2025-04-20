package org.chomookun.arch4j.web.api.v1.page.dto;

import lombok.*;
import org.chomookun.arch4j.core.page.entity.PageWidgetEntity;
import org.chomookun.arch4j.core.page.model.PageWidget;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageWidgetResponse {

    private String pageId;

    private Integer index;

    private String type;

    private String properties;

    private String url;

    public static PageWidgetResponse from(PageWidget pageWidget) {
        return PageWidgetResponse.builder()
                .pageId(pageWidget.getPageId())
                .index(pageWidget.getIndex())
                .type(pageWidget.getType())
                .properties(pageWidget.getProperties())
                .url(pageWidget.getUrl())
                .build();
    }

}
