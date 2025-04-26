package org.chomookun.arch4j.core.git.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseModel;
import org.chomookun.arch4j.core.common.data.converter.BooleanConverter;
import org.chomookun.arch4j.core.git.entity.GitEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Git extends BaseModel {

    private String gitId;

    private String name;

    private String url;

    private String branch;

    private boolean discussionEnabled;

    private String discussionId;

    private String note;

    public static Git from(GitEntity gitEntity) {
        return Git.builder()
                .systemRequired(gitEntity.isSystemRequired())
                .systemUpdatedAt(gitEntity.getSystemUpdatedAt())
                .systemUpdatedBy(gitEntity.getSystemUpdatedBy())
                .gitId(gitEntity.getGitId())
                .name(gitEntity.getName())
                .url(gitEntity.getUrl())
                .branch(gitEntity.getBranch())
                .discussionEnabled(gitEntity.isDiscussionEnabled())
                .discussionId(gitEntity.getDiscussionId())
                .note(gitEntity.getNote())
                .build();
    }

}
