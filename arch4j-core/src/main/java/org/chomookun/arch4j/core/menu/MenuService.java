package org.chomookun.arch4j.core.menu;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.menu.entity.MenuEntity;
import org.chomookun.arch4j.core.menu.entity.MenuEntity_;
import org.chomookun.arch4j.core.menu.model.MenuRole;
import org.chomookun.arch4j.core.menu.repository.MenuRepository;
import org.chomookun.arch4j.core.menu.entity.MenuRoleEntity;
import org.chomookun.arch4j.core.menu.model.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final MenuRepository menuRepository;

    private final StringRedisTemplate redisTemplate;

    /**
     * Evicts cache for menu
     * @param menuId menu id
     */
    void evictCache(String menuId) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                redisTemplate.convertAndSend(MenuChannels.MENU_EVICT, menuId);
            }
        });
    }

    /**
     * Saves menu
     * @param menu menu
     * @return saved menu
     */
    @Transactional
    public Menu saveMenu(Menu menu) {
        MenuEntity menuEntity;
        if (menu.getMenuId() == null) {
            menuEntity = MenuEntity.builder()
                    .menuId(IdGenerator.uuid())
                    .build();
        } else {
            menuEntity = menuRepository.findById(menu.getMenuId()).orElseThrow();
        }
        menuEntity.setSystemUpdatedAt(LocalDateTime.now()); // disable dirty checking
        menuEntity.setName(menu.getName());
        menuEntity.setParentMenuId(menu.getParentMenuId());
        menuEntity.setLink(menu.getLink());
        menuEntity.setTarget(menu.getTarget());
        menuEntity.setIcon(menu.getIcon());
        menuEntity.setSort(menu.getSort());
        menuEntity.setNote(menu.getNote());
        menuEntity.setEnabled(menu.isEnabled());

        // menu roles
        menuEntity.getRoles().clear();
        menu.getViewRoles().forEach(viewRole -> {
            MenuRoleEntity menuRoleEntity = MenuRoleEntity.builder()
                    .menuId(menuEntity.getMenuId())
                    .roleId(viewRole.getRoleId())
                    .type(MenuRole.Type.VIEW)
                    .build();
            menuEntity.getRoles().add(menuRoleEntity);
        });
        menu.getLinkRoles().forEach(linkRole -> {
            MenuRoleEntity menuRoleEntity = MenuRoleEntity.builder()
                    .menuId(menuEntity.getMenuId())
                    .roleId(linkRole.getRoleId())
                    .type(MenuRole.Type.LINK)
                    .build();
            menuEntity.getRoles().add(menuRoleEntity);
        });

        // saves and returns
        MenuEntity savedMenu = menuRepository.saveAndFlush(menuEntity);
        entityManager.refresh(savedMenu);
        evictCache(savedMenu.getMenuId());
        return Menu.from(savedMenu);
    }

    /**
     * Gets menu
     * @param menuId menu id
     * @return menu
     */
    public Optional<Menu> getMenu(String menuId) {
        return menuRepository.findById(menuId)
                .map(Menu::from);
    }

    /**
     * Gets menus
     * @return menus
     */
    public List<Menu> getMenus() {
        Sort sort = Sort.by(Sort.Order.asc(MenuEntity_.SORT)); // bug: nullsLast not work
        List<MenuEntity> menuEntities = menuRepository.findAll(sort);
        return menuEntities.stream()
                .map(Menu::from)
                .collect(Collectors.toList());
    }

    /**
     * Deletes menu
     * @param menuId menu id
     */
    @Transactional
    public void deleteMenu(String menuId) {
        MenuEntity menuEntity = menuRepository.findById(menuId).orElseThrow();
        menuRepository.delete(menuEntity);
        menuRepository.flush();
        evictCache(menuId);
    }

}
