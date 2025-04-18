package org.chomookun.arch4j.core.page.model;

import lombok.*;
import org.chomookun.arch4j.core.page.entity.PageWidgetEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageWidget {

    private String pageId;

    private Integer index;

    private String type;

    private String properties;

    private String url;

    public static PageWidget from(PageWidgetEntity pagePanelEntity) {
        return PageWidget.builder()
                .pageId(pagePanelEntity.getPageId())
                .index(pagePanelEntity.getIndex())
                .type(pagePanelEntity.getType())
                .properties(pagePanelEntity.getProperties())
                .build();
    }

}
