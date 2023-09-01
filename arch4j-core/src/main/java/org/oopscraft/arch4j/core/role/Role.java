package org.oopscraft.arch4j.core.role;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldModel;
import org.oopscraft.arch4j.core.role.dao.RoleAuthorityEntity;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Role extends SystemFieldModel {

    private String roleId;

    private String roleName;

    private boolean anonymous;

    private boolean authenticated;

    private String note;

    @Builder.Default
	private List<Authority> authorities = new ArrayList<>();

    public static Role from(RoleEntity roleEntity) {
        Role role = Role.builder()
                .systemRequired(roleEntity.isSystemRequired())
                .systemUpdatedAt(roleEntity.getSystemUpdatedAt())
                .systemUpdatedBy(roleEntity.getSystemUpdatedBy())
                .roleId(roleEntity.getRoleId())
                .roleName(roleEntity.getRoleName())
                .anonymous(roleEntity.isAnonymous())
                .authenticated(roleEntity.isAuthenticated())
                .build();

        List<Authority> authorities = roleEntity.getRoleAuthorityEntities().stream()
                .map(RoleAuthorityEntity::getAuthorityEntity)
                .filter(Objects::nonNull)
                .map(Authority::from)
                .collect(Collectors.toList());

        role.setAuthorities(authorities);
        return role;
    }

}
