package org.chomookun.arch4j.core.role.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;

import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "core_authority")
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthorityEntity extends BaseEntity {

    @Id
    @Column(name = "authority_id", length = 32)
    @Comment("Authority ID")
    private String authorityId;

    @Column(name = "name")
    @Comment("Name")
    private String name;

    @Column(name = "note", length = 4000)
    @Lob
    @Comment("Note")
    private String note;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "authority_id", updatable = false)
    @Builder.Default
    private List<RoleAuthorityEntity> roleAuthorities = new ArrayList<>();

}
