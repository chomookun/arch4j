package org.chomookun.arch4j.core.menu.repository;

import org.chomookun.arch4j.core.menu.entity.MenuEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<MenuEntity,String>, JpaSpecificationExecutor<MenuEntity> {

}
