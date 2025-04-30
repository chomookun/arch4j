package org.chomookun.arch4j.web.api.v1.menu.dto;

import lombok.*;
import org.chomookun.arch4j.core.menu.model.Menu;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuResponse {

    private String menuId;

    private String menuName;

    private String parentMenuId;

    private String link;

    private Menu.Target target;

    private String icon;

    private Integer sort;

    public static MenuResponse from(Menu menu) {
        return MenuResponse.builder()
                .menuId(menu.getMenuId())
                .menuName(menu.getName())
                .parentMenuId(menu.getParentMenuId())
                .link(menu.getLink())
                .target(menu.getTarget())
                .icon(menu.getIcon())
                .sort(menu.getSort())
                .build();
    }

}
