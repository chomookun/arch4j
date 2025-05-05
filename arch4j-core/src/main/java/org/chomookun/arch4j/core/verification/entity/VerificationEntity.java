package org.chomookun.arch4j.core.verification.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.data.converter.BooleanConverter;

@Entity
@Table(name = "core_verification")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VerificationEntity extends BaseEntity {

    @Id
    @Column(name = "verification_id", length = 32)
    private String verificationId;

    @Column(name = "name")
    private String name;

    @Column(name = "verifier_type", length = 32)
    private String verifierType;

    @Column(name = "verifier_config", length = Integer.MAX_VALUE)
    @Lob
    private String verifierConfig;

    @Column(name = "enabled", length = 1)
    @Convert(converter = BooleanConverter.class)
    private boolean enabled;

}
