package org.oopscraft.arch4j.core.git.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.common.data.BaseModel;
import org.oopscraft.arch4j.core.git.dao.GitEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Git extends BaseModel {

    private String gitId;

    private String name;

    private String note;

    private String url;

    private String branch;

    public static Git from(GitEntity gitEntity) {
        return Git.builder()
                .systemRequired(gitEntity.isSystemRequired())
                .systemUpdatedAt(gitEntity.getSystemUpdatedAt())
                .systemUpdatedBy(gitEntity.getSystemUpdatedBy())
                .gitId(gitEntity.getGitId())
                .name(gitEntity.getName())
                .note(gitEntity.getNote())
                .url(gitEntity.getUrl())
                .branch(gitEntity.getBranch())
                .build();
    }

}
