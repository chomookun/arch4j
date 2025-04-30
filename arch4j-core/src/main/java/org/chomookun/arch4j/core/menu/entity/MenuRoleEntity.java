package org.chomookun.arch4j.core.menu.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.data.converter.GenericEnumConverter;
import org.chomookun.arch4j.core.menu.model.MenuRole;
import org.chomookun.arch4j.core.security.entity.RoleEntity;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.io.Serializable;

@Entity
@Table(name = "core_menu_role")
@IdClass(MenuRoleEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuRoleEntity extends BaseEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String menuId;
        private String roleId;
        private MenuRole.Type type;
    }

    @Id
    @Column(name = "menu_id", length = 32)
    private String menuId;

    @Id
    @Column(name = "role_id", length = 32)
    private String roleId;

    @Id
    @Column(name = "type", length = 16)
    @Enumerated(EnumType.STRING)    // @Convert is not working at @Id field
    @Type(TypeConverter.class)      // force to sql type to string
    private MenuRole.Type type;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "menu_id", insertable = false, updatable = false)
    private MenuEntity menuEntity;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private RoleEntity roleEntity;

    /**
     * Type converter for MenuRole.Type
     */
    @Converter(autoApply = true)
    public static class TypeConverter extends GenericEnumConverter<MenuRole.Type> {}

}
