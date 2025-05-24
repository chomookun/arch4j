package org.chomookun.arch4j.core.discussion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;

@Entity
@Table(name = "core_discussion")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DiscussionEntity extends BaseEntity {

    @Id
    @Column(name = "discussion_id", length = 32)
    private String discussionId;

    @Column(name = "name")
    private String name;

    @Column(name = "provider_type", length = 32)
    private String providerType;

    @Column(name = "provider_properties", length = Integer.MAX_VALUE)
    private String providerProperties;

}
