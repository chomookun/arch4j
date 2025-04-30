package org.chomookun.arch4j.core.security.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;

import jakarta.persistence.*;

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
    private String authorityId;

    @Column(name = "name")
    private String name;

    @Column(name = "description", length = 4000)
    @Lob
    private String description;

}
