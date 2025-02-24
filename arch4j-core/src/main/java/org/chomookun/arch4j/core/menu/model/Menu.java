package org.chomookun.arch4j.core.menu.model;

import jakarta.persistence.Converter;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseModel;
import org.chomookun.arch4j.core.common.data.converter.AbstractEnumConverter;
import org.chomookun.arch4j.core.menu.entity.MenuEntity;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Menu extends BaseModel {

    private String menuId;

    private String name;

    private String parentMenuId;

    private String link;

    private Target target;

    private String icon;

    private Integer sort;

    private String note;

    @Builder.Default
    private List<MenuRole> viewMenuRoles = new ArrayList<>();

    @Builder.Default
    private List<MenuRole> linkMenuRoles = new ArrayList<>();

    public enum Target { SELF, BLANK }

    @Converter(autoApply = true)
    public static class TargetConverter extends AbstractEnumConverter<Target> {}

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
                .name(menuEntity.getName())
                .parentMenuId(menuEntity.getParentMenuId())
                .link(menuEntity.getLink())
                .target(menuEntity.getTarget())
                .icon(menuEntity.getIcon())
                .sort(menuEntity.getSort())
                .note(menuEntity.getNote())
                .build();
        menu.setViewMenuRoles(menuEntity.getMenuRoles().stream()
                .filter(menuRoleEntity -> menuRoleEntity.getType() == MenuRole.Type.VIEW)
                .map(MenuRole::from)
                .toList());
        menu.setLinkMenuRoles(menuEntity.getMenuRoles().stream()
                .filter(menuRoleEntity -> menuRoleEntity.getType() == MenuRole.Type.LINK)
                .map(MenuRole::from)
                .toList());
        return menu;
    }

}
