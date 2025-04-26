package org.chomookun.arch4j.core.discussion.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseModel;
import org.chomookun.arch4j.core.discussion.entity.DiscussionEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Discussion extends BaseModel {

    private String discussionId;

    @NotBlank
    private String name;

    private String discussionProviderId;

    private String discussionProviderConfig;

    public static Discussion from(DiscussionEntity discussionEntity) {
        return Discussion.builder()
                .discussionId(discussionEntity.getDiscussionId())
                .name(discussionEntity.getName())
                .discussionProviderId(discussionEntity.getDiscussionProviderId())
                .discussionProviderConfig(discussionEntity.getDiscussionProviderConfig())
                .build();
    }

}
