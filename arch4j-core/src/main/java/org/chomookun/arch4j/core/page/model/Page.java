package org.chomookun.arch4j.core.page.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseModel;
import org.chomookun.arch4j.core.page.entity.PageEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Page extends BaseModel {

    private String pageId;

    private String name;

    private String content;

    private ContentFormat contentFormat;

    @Builder.Default
    private List<PageWidget> pageWidgets = new ArrayList<>();

    public enum ContentFormat { TEXT, MARKDOWN, HTML }

    public static Page from(PageEntity pageEntity) {
        return Page.builder()
                .systemRequired(pageEntity.isSystemRequired())
                .systemUpdatedAt(pageEntity.getSystemUpdatedAt())
                .systemUpdatedBy(pageEntity.getSystemUpdatedBy())
                .pageId(pageEntity.getPageId())
                .name(pageEntity.getName())
                .content(pageEntity.getContent())
                .contentFormat(pageEntity.getContentFormat())
                .pageWidgets(pageEntity.getPageWidgets().stream()
                        .map(PageWidget::from)
                        .collect(Collectors.toList()))
                .build();
    }

}
