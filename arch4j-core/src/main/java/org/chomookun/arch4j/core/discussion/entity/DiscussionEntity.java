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

    @Column(name = "discussion_provider_id", length = 32)
    private String discussionProviderId;

    @Column(name = "discussion_provider_config", length = Integer.MAX_VALUE)
    private String discussionProviderConfig;

}
