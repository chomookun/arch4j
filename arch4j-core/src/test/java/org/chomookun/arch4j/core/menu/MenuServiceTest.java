package org.chomookun.arch4j.core.menu;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.menu.entity.MenuEntity;
import org.chomookun.arch4j.core.menu.model.Menu;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class MenuServiceTest extends CoreTestSupport {

    private final MenuService menuService;

    @Test
    void saveMenuForPersist() {
        // given
        Menu menu = Menu.builder()
                .menuId(IdGenerator.uuid())
                .name("test menu")
                .link("test/link")
                .target(Menu.Target.SELF)
                .build();
        // when
        Menu savedMenu = menuService.saveMenu(menu);
        // then
        assertNotNull(entityManager.find(MenuEntity.class, savedMenu.getMenuId()));
    }

    @Test
    void saveMenuForMerge() {
        // given
        MenuEntity menuEntity = MenuEntity. builder()
                .menuId(IdGenerator.uuid())
                .name("test menu")
                .build();
        entityManager.persist(menuEntity);
        entityManager.flush();
        // when
        Menu menu = menuService.getMenu(menuEntity.getMenuId()).orElseThrow();
        menu.setName("changed");
        Menu savedMenu = menuService.saveMenu(menu);
        // then
        assertEquals("changed", entityManager.find(MenuEntity.class, savedMenu.getMenuId()).getName());
    }

    @Test
    void getMenu() {
        // given
        MenuEntity menuEntity = MenuEntity.builder()
                .menuId(IdGenerator.uuid())
                .name("test menu")
                .build();
        entityManager.persist(menuEntity);
        entityManager.flush();
        // when
        Menu menu = menuService.getMenu(menuEntity.getMenuId()).orElseThrow();
        // then
        assertEquals(menuEntity.getMenuId(), menu.getMenuId());
    }

    @Test
    void deleteMenu() {
        // given
        MenuEntity menuEntity = MenuEntity.builder()
                .menuId(IdGenerator.uuid())
                .name("test menu")
                .build();
        entityManager.persist(menuEntity);
        entityManager.flush();
        // when
        menuService.deleteMenu(menuEntity.getMenuId());
        // then
        MenuEntity deletedMenu = entityManager.find(MenuEntity.class, menuEntity.getMenuId());
        assertNull(deletedMenu);
    }

    @Test
    void getMenus() {
        // given
        MenuEntity menuEntity = MenuEntity.builder()
                .menuId(IdGenerator.uuid())
                .name("test menu")
                .build();
        entityManager.persist(menuEntity);
        entityManager.flush();
        // when
        List<Menu> menus = menuService.getMenus();
        // then
        assertFalse(menus.isEmpty());
    }

}