package org.oopscraft.arch4j.core.page;

import lombok.*;
import org.oopscraft.arch4j.core.page.dao.PageEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Page {

    private String pageId;

    private String name;

    @Builder.Default
    private ContentFormat contentFormat = ContentFormat.TEXT;

    private String content;

    @Builder.Default
    private List<PageWidget> widgets = new ArrayList<>();

    public static Page from(PageEntity pageEntity) {
        return Page.builder()
                .pageId(pageEntity.getPageId())
                .name(pageEntity.getName())
                .contentFormat(pageEntity.getContentFormat())
                .content(pageEntity.getContent())
                .widgets(pageEntity.getWidgets().stream()
                        .map(PageWidget::from)
                        .collect(Collectors.toList()))
                .build();
    }

}
