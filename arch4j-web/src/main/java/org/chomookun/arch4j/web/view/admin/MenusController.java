package org.chomookun.arch4j.web.view.admin;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.menu.model.Menu;
import org.chomookun.arch4j.core.menu.MenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("admin/menus")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('admin.menus')")
public class MenusController {

    private final MenuService menuService;

    /**
     * Returns menus model and view
     * @return model and view
     */
    @GetMapping
    public ModelAndView menus() {
        ModelAndView modelAndView = new ModelAndView("admin/menus.html");
        modelAndView.addObject("menuTargets", Menu.Target.values());
        return modelAndView;
    }

    /**
     * Returns all menus
     * @return all pages
     */
    @GetMapping("get-menus")
    @ResponseBody
    public List<Menu> getMenus() {
        return menuService.getMenus();
    }

    /**
     * Returns specified menu
     * @param menuId menu id
     * @return menu
     */
    @GetMapping("get-menu")
    @ResponseBody
    public Menu getMenu(@RequestParam("menuId")String menuId) {
        return menuService.getMenu(menuId)
                .orElseThrow();
    }

    /**
     * Saves menu
     * @param menu menu
     * @return saved menu
     */
    @PostMapping("save-menu")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.menus.edit')")
    public Menu saveMenu(@RequestBody @Valid Menu menu) {
        if(menu.getMenuId() == null) {
            menu.setMenuId(IdGenerator.uuid());
        }
        return menuService.saveMenu(menu);
    }

    /**
     * Deletes specified menu
     * @param menuId menu id
     */
    @GetMapping("delete-menu")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('admin.menus.edit')")
    public void deleteMenu(@RequestParam("menuId")String menuId) {
        menuService.deleteMenu(menuId);
    }

}
