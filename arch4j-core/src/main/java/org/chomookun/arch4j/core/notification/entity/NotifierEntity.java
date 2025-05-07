package org.chomookun.arch4j.core.notification.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;

import jakarta.persistence.*;

@Entity
@Table(name = "core_notifier")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NotifierEntity extends BaseEntity {

    @Id
    @Column(name = "notifier_id", length = 32)
    private String notifierId;

    @Column(name = "name")
    private String name;

    @Column(name = "client_type", length = 32)
    private String clientType;

    @Column(name = "client_config", length = Integer.MAX_VALUE)
    @Lob
    private String clientConfig;

}
