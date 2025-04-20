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

    @Builder.Default
    private Format format = Format.TEXT;

    private String content;

    @Builder.Default
    private List<PageWidget> pageWidgets = new ArrayList<>();

    public enum Format { TEXT, MARKDOWN }

    public static Page from(PageEntity pageEntity) {
        return Page.builder()
                .systemRequired(pageEntity.isSystemRequired())
                .systemUpdatedAt(pageEntity.getSystemUpdatedAt())
                .systemUpdatedBy(pageEntity.getSystemUpdatedBy())
                .pageId(pageEntity.getPageId())
                .name(pageEntity.getName())
                .format(pageEntity.getContentFormat())
                .content(pageEntity.getContent())
                .pageWidgets(pageEntity.getPageWidgets().stream()
                        .map(PageWidget::from)
                        .collect(Collectors.toList()))
                .build();
    }

}
