package org.chomookun.arch4j.core.menu.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.data.converter.BooleanConverter;
import org.chomookun.arch4j.core.common.data.converter.GenericEnumConverter;
import org.chomookun.arch4j.core.common.i18n.I18nSupport;
import org.chomookun.arch4j.core.menu.model.Menu;

import jakarta.persistence.*;

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
public class MenuEntity extends BaseEntity implements I18nSupport<MenuI18nEntity> {

    @Id
    @Column(name = "menu_id", length = 32)
    private String menuId;

    /**
     * Sets name
     * @param name name
     */
    public void setName(String name) {
        i18nSet(i18n -> i18n.setName(name));
    }

    /**
     * Gets localized value of name
     * @return localized name
     */
    public String getName() {
        return i18nGet(MenuI18nEntity::getName);
    }

    @Column(name = "parent_menu_id", length = 32)
    private String parentMenuId;

    @Column(name = "link")
    private String link;

    @Column(name = "target", length = 8)
    private Menu.Target target;

    @Column(name = "icon", length = 4000)
    @Lob
    private String icon;

    @Column(name = "sort")
    private Integer sort;

    @Column(name = "note", length = 4000)
    @Lob
    private String note;

    @Column(name = "enabled", length = 1)
    @Convert(converter = BooleanConverter.class)
    private boolean enabled;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(referencedColumnName = "menu_id", name = "menu_id", updatable = false)
    @Builder.Default
    private List<MenuI18nEntity> i18ns = new ArrayList<>();

    @Override
    public MenuI18nEntity provideNewI18n(String locale) {
        return MenuI18nEntity.builder()
                .menuId(this.menuId)
                .locale(locale)
                .build();
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_menu_id", insertable = false, updatable = false)
    private MenuEntity parentMenu;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(referencedColumnName = "menu_id", name = "menu_id", insertable = false, updatable = false)
    @Builder.Default
    private List<MenuRoleEntity> roles = new ArrayList<>();

    /**
     * Target converter
     */
    @Converter(autoApply = true)
    public static class TargetConverter extends GenericEnumConverter<Menu.Target> {}

}
