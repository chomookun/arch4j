package org.chomookun.arch4j.core.page.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.i18n.test1.I18n;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_page_i18n")
@IdClass(PageI18nEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageI18nEntity extends BaseEntity implements I18n {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String pageId;
        private String locale;
    }

    @Id
    @Column(name = "page_id", length = 32)
    private String pageId;

    @Id
    @Column(name = "locale", length = 8)
    private String locale;

    @Column(name = "content", length = Integer.MAX_VALUE)
    @Lob
    private String content;

}

