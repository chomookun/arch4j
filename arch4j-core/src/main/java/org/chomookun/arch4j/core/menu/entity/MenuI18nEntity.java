package org.chomookun.arch4j.core.menu.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.i18n.I18nEntity;
import org.chomookun.arch4j.core.common.data.BaseEntity;

import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

@Entity
@Table(name = "core_menu_i18n")
//@IdClass(MenuI18nEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuI18nEntity extends BaseEntity implements I18nEntity {

    @Embeddable
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Id implements Serializable {

        @Column(name = "menu_id", length = 32)
        @Comment("Menu ID")
        private String menuId;

        @Column(name = "language", length = 8)
        @Comment("Language")
        private String language;
    }

    @EmbeddedId
    private Id id;

//    @Id
//    @Column(name = "menu_id", length = 32)
//    @Comment("Menu ID")
//    private String menuId;
//
//    @Id
//    @Column(name = "language", length = 8)
//    @Comment("Language")
//    private String language;

    @Column(name = "name")
    @Comment("Name")
    private String name;

    @Override
    public String getLanguage() {
        return id.getLanguage();
    }

}

