package org.chomookun.arch4j.core.menu.repository;

import org.chomookun.arch4j.core.menu.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity,String>, JpaSpecificationExecutor<MenuEntity> {

}
