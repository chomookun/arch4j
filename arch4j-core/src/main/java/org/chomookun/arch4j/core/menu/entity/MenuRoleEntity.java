package org.chomookun.arch4j.core.menu.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.data.converter.AbstractEnumConverter;
import org.chomookun.arch4j.core.menu.model.MenuRole;
import org.chomookun.arch4j.core.role.entity.RoleEntity;

import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

@Entity
@Table(name = "core_menu_role")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuRoleEntity extends BaseEntity {

    @Embeddable
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Id implements Serializable {

        @Column(name = "menu_id")
        @Comment("Menu ID")
        private String menuId;

        @Column(name = "role_id")
        @Comment("Role ID")
        private String roleId;

        @Column(name = "type")
        @Convert(converter = TypeConverter.class)
        @Comment("Type")
        private MenuRole.Type type;
    }

    @EmbeddedId
    private Id id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "menu_id", insertable = false, updatable = false)
    private MenuEntity menuEntity;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private RoleEntity roleEntity;

    @Converter
    public static class TypeConverter extends AbstractEnumConverter<MenuRole.Type> {}

}
