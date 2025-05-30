package org.chomookun.arch4j.core.page.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.data.converter.GenericEnumConverter;
import org.chomookun.arch4j.core.common.i18n.test1.I18nEntitySupport;
import org.chomookun.arch4j.core.page.model.Page;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "core_page")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageEntity extends BaseEntity implements I18nEntitySupport<PageI18nEntity> {

    @Id
    @Column(name = "page_id", length = 32)
    private String pageId;

    @Column(name = "name")
    private String name;

    @Column(name = "content", length = Integer.MAX_VALUE)
    @Lob
    private String content;

    @Column(name = "content_format", length = 16)
    private Page.ContentFormat contentFormat;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "page_id", updatable = false)
    @OrderBy(PageWidgetEntity_.INDEX)
    @Builder.Default
    private List<PageWidgetEntity> pageWidgets = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "page_id", updatable = false)
    @Builder.Default
    private List<PageI18nEntity> i18ns = new ArrayList<>();

    @Converter(autoApply = true)
    public static class ContentFormatConverter extends GenericEnumConverter<Page.ContentFormat> {}

}
