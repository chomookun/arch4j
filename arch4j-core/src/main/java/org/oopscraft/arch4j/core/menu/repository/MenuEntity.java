package org.oopscraft.arch4j.core.menu.repository;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.menu.MenuTarget;
import org.oopscraft.arch4j.core.role.repository.RoleEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "core_menu",
    indexes = {
        @Index(name = "ix_parent_menu_id", columnList = "parent_menu_id")
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuEntity extends SystemFieldEntity {

    @Id
    @Column(name = "menu_id", length = 32)
    private String menuId;

    @Column(name = "parent_menu_id", length = 32)
    private String parentMenuId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_menu_id", referencedColumnName = "menu_id", insertable = false, updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MenuEntity parentMenu;

    @Column(name = "name")
    private String name;

    @Column(name = "link")
    private String link;

    @Column(name = "target", length = 8)
    private MenuTarget target;

    @Column(name = "icon")
    @Lob
    private String icon;

    @Column(name = "sort")
    private Integer sort;

    @Column(name = "note")
    @Lob
    private String note;

    @ManyToMany
    @JoinTable(
            name = "core_menu_role",
            joinColumns = @JoinColumn(name = "menu_id"),
            foreignKey = @ForeignKey(name = "none"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            inverseForeignKey = @ForeignKey(name = "none")
    )
    @Builder.Default
    List<RoleEntity> roles = new ArrayList<>();

    @OneToMany(mappedBy = MenuI18nEntity_.MENU_ID, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MenuI18nEntity> i18ns = new ArrayList<>();

}
