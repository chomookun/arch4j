package org.chomookun.arch4j.core.menu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.common.data.BaseModel;
import org.chomookun.arch4j.core.common.i18n.I18nSupport;
import org.chomookun.arch4j.core.menu.entity.MenuEntity;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class Menu extends BaseModel implements I18nSupport<MenuI18n> {

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
        return i18nGet(MenuI18n::getName);
    }

    private String parentMenuId;

    private String link;

    private Target target;

    private String icon;

    private Integer sort;

    private String note;

    private boolean enabled;

    @Builder.Default
    @JsonIgnore
    private List<MenuI18n> i18ns = new ArrayList<>();

    @Override
    public MenuI18n provideNewI18n(String locale) {
        return MenuI18n.builder()
                .menuId(this.menuId)
                .locale(locale)
                .build();
    }

    @Builder.Default
    private List<MenuRole> viewRoles = new ArrayList<>();

    @Builder.Default
    private List<MenuRole> linkRoles = new ArrayList<>();

    public enum Target { SELF, BLANK }

    /**
     * menu factory method
     * @param menuEntity menu entity
     * @return menu
     */
    public static Menu from(MenuEntity menuEntity) {
        Menu menu = Menu.builder()
                .systemRequired(menuEntity.isSystemRequired())
                .systemUpdatedAt(menuEntity.getSystemUpdatedAt())
                .systemUpdatedBy(menuEntity.getSystemUpdatedBy())
                .menuId(menuEntity.getMenuId())
                .parentMenuId(menuEntity.getParentMenuId())
                .link(menuEntity.getLink())
                .target(menuEntity.getTarget())
                .icon(menuEntity.getIcon())
                .sort(menuEntity.getSort())
                .note(menuEntity.getNote())
                .enabled(menuEntity.isEnabled())
                .build();
        menu.setI18ns(menuEntity.getI18ns().stream()
                .map(MenuI18n::from)
                .toList());
        menu.setViewRoles(menuEntity.getRoles().stream()
                .filter(menuRoleEntity -> menuRoleEntity.getType() == MenuRole.Type.VIEW)
                .map(MenuRole::from)
                .toList());
        menu.setLinkRoles(menuEntity.getRoles().stream()
                .filter(menuRoleEntity -> menuRoleEntity.getType() == MenuRole.Type.LINK)
                .map(MenuRole::from)
                .toList());
        return menu;
    }

}
