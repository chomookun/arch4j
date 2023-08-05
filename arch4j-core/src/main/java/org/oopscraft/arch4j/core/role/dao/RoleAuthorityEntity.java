package org.oopscraft.arch4j.core.role.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_role_authority")
@IdClass(RoleAuthorityEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleAuthorityEntity extends SystemFieldEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String roleId;
        private String authorityId;
    }

    @Id
    @Column(name = "role_id")
    private String roleId;

    @Id
    @Column(name = "authority_id")
    private String authorityId;

}
