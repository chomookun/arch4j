package org.chomookun.arch4j.core.security.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_role_authority")
@IdClass(RoleAuthorityEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleAuthorityEntity extends BaseEntity {

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

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private RoleEntity roleEntity;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "authority_id", insertable = false, updatable = false)
    private AuthorityEntity authorityEntity;

}
