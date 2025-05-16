package org.chomookun.arch4j.core.menu.model;

import lombok.*;
import org.chomookun.arch4j.core.common.i18n.I18n;
import org.chomookun.arch4j.core.menu.entity.MenuI18nEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuI18n implements I18n {

    private String menuId;

    private String locale;

    private String name;

    public static MenuI18n from(MenuI18nEntity menuI18nEntity) {
        return MenuI18n.builder()
                .menuId(menuI18nEntity.getMenuId())
                .locale(menuI18nEntity.getLocale())
                .name(menuI18nEntity.getName())
                .build();
    }

}

