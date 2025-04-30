package org.chomookun.arch4j.core.security.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseModel;
import org.chomookun.arch4j.core.security.entity.RoleAuthorityEntity;
import org.chomookun.arch4j.core.security.entity.RoleEntity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Role extends BaseModel {

    @NotNull
    private String roleId;

    @NotBlank
    private String name;

    private boolean anonymous;

    private boolean authenticated;

    private String description;

    @Builder.Default
	private List<Authority> authorities = new ArrayList<>();

    /**
     * role factory method
     * @param roleEntity role entity
     * @return role
     */
    public static Role from(RoleEntity roleEntity) {
        // authorities
        List<Authority> authorities = roleEntity.getAuthorities().stream()
                .map(RoleAuthorityEntity::getAuthorityEntity)
                .filter(Objects::nonNull)
                .map(Authority::from)
                .toList();
        // returns
        return Role.builder()
                .systemRequired(roleEntity.isSystemRequired())
                .systemUpdatedAt(roleEntity.getSystemUpdatedAt())
                .systemUpdatedBy(roleEntity.getSystemUpdatedBy())
                .roleId(roleEntity.getRoleId())
                .name(roleEntity.getName())
                .anonymous(roleEntity.isAnonymous())
                .authenticated(roleEntity.isAuthenticated())
                .description(roleEntity.getDescription())
                .authorities(authorities)
                .build();
    }

}
