package org.chomookun.arch4j.web.view.admin;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.menu.model.Menu;
import org.chomookun.arch4j.core.menu.MenuService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@ConditionalOnProperty(prefix = "web.admin", name = "enabled", havingValue = "true", matchIfMissing = false)
@RequestMapping("admin/menu")
@PreAuthorize("hasAuthority('admin.menu')")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ModelAndView menus() {
        ModelAndView modelAndView = new ModelAndView("admin/menu");
        modelAndView.addObject("menuTargets", Menu.Target.values());
        return modelAndView;
    }

    @GetMapping("get-menus")
    @ResponseBody
    public List<Menu> getMenus() {
        return menuService.getMenus();
    }

    @GetMapping("get-menu")
    @ResponseBody
    public Menu getMenu(@RequestParam("menuId")String menuId) {
        return menuService.getMenu(menuId).orElseThrow();
    }

    @PostMapping("save-menu")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.menu:edit')")
    public Menu saveMenu(@RequestBody @Valid Menu menu) {
        return menuService.saveMenu(menu);
    }

    @GetMapping("delete-menu")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.menu:edit')")
    public void deleteMenu(@RequestParam("menuId")String menuId) {
        menuService.deleteMenu(menuId);
    }

}
