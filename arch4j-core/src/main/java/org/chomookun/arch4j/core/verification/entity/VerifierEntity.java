package org.chomookun.arch4j.core.verification.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.data.converter.BooleanConverter;

@Entity
@Table(name = "core_verifier")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VerifierEntity extends BaseEntity {

    @Id
    @Column(name = "verifier_id", length = 32)
    private String verifierId;

    @Column(name = "name")
    private String name;

    @Column(name = "client_type", length = 32)
    private String clientType;

    @Column(name = "client_properties", length = Integer.MAX_VALUE)
    @Lob
    private String clientProperties;

    @Column(name = "enabled", length = 1)
    @Convert(converter = BooleanConverter.class)
    private boolean enabled;

    @Column(name = "note", length = 4000)
    @Lob
    private String note;

}
