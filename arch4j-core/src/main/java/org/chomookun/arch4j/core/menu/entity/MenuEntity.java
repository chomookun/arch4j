package org.chomookun.arch4j.core.menu.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.data.converter.AbstractEnumConverter;
import org.chomookun.arch4j.core.common.i18n.I18nGetter;
import org.chomookun.arch4j.core.common.i18n.I18nSetter;
import org.chomookun.arch4j.core.common.i18n.I18nSupportEntity;
import org.chomookun.arch4j.core.menu.model.Menu;

import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "core_menu",
    indexes = {
        @Index(name = "ix_parent_menu_id", columnList = "parent_menu_id")
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuEntity extends BaseEntity implements I18nSupportEntity<MenuI18nEntity> {

    @Id
    @Column(name = "menu_id", length = 32)
    @Comment("Menu ID")
    private String menuId;

    @Column(name = "name")
    @Comment("Name")
    private String name;

    @Column(name = "parent_menu_id", length = 32)
    @Comment("Parent Menu ID")
    private String parentMenuId;

    @Column(name = "link")
    @Comment("a href")
    private String link;

    @Column(name = "target", length = 8)
    @Convert(converter = TargetConverter.class)
    @Comment("a href target")
    private Menu.Target target;

    @Column(name = "icon", length = 4000)
    @Lob
    @Comment("Icon")
    private String icon;

    @Column(name = "sort")
    @Comment("Sort")
    private Integer sort;

    @Column(name = "note", length = 4000)
    @Lob
    @Comment("Note")
    private String note;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_menu_id", referencedColumnName = "menu_id", insertable = false, updatable = false)
    private MenuEntity parentMenu;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id", updatable = false)
    @Builder.Default
    private List<MenuRoleEntity> menuRoles = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id", updatable = false)
    @Builder.Default
    private List<MenuI18nEntity> menuI18ns = new ArrayList<>();

    @Override
    public List<MenuI18nEntity> provideI18nEntities() {
        return this.menuI18ns;
    }

    @Override
    public MenuI18nEntity provideNewI18nEntity(String language) {
        return MenuI18nEntity.builder()
                .id(MenuI18nEntity.Id.builder()
                        .menuId(this.menuId)
                        .language(language)
                        .build())
                .build();
    }

    public void setName(String name) {
        I18nSetter.of(this, this.name)
                .whenDefault(() -> this.name = name)
                .whenI18n(menuLanguageEntity -> menuLanguageEntity.setName(name))
                .set();
    }

    public String getName() {
        return I18nGetter.of(this, this.name)
                .whenDefault(() -> this.name)
                .whenI18n(MenuI18nEntity::getName)
                .get();
    }

    @Converter
    public static class TargetConverter extends AbstractEnumConverter<Menu.Target> {}

}
