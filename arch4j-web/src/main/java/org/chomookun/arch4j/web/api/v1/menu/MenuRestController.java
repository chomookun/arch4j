package org.chomookun.arch4j.web.api.v1.menu;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.menu.CachedMenuService;
import org.chomookun.arch4j.core.menu.model.Menu;
import org.chomookun.arch4j.web.api.v1.menu.dto.MenuResponse;
import org.chomookun.arch4j.core.security.SecurityUtils;
import org.chomookun.arch4j.core.common.data.PageableUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "menu")
@RestController
@RequestMapping("api/v1/menus")
@RequiredArgsConstructor
public class MenuRestController {

    private final CachedMenuService cachedMenuService;

    @Operation(summary = "Returns list of all menu")
    @GetMapping
    public ResponseEntity<List<MenuResponse>> getMenus() {
        List<MenuResponse> menuResponses = cachedMenuService.getMenus().stream()
                .filter(menu -> SecurityUtils.hasPermission(menu.getViewRoles()))
                .peek(menu -> {
                    if (!SecurityUtils.hasPermission(menu.getLinkRoles())) {
                        menu.setLink(null);
                        menu.setTarget(null);
                    }
                })
                .map(MenuResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_RANGE, PageableUtils.toContentRange("menu",  null, menuResponses.size()))
                .body(menuResponses);
    }

    @Operation(summary = "Returns the specified menu")
    @Parameter(name = "menuId", in = ParameterIn.PATH)
    @GetMapping("{menuId}")
    public ResponseEntity<MenuResponse> getMenu(@PathVariable("menuId") String menuId) {
        Menu menu = cachedMenuService.getMenu(menuId).orElseThrow();
        MenuResponse menuResponse = MenuResponse.from(menu);
        return ResponseEntity.ok(menuResponse);
    }

}
