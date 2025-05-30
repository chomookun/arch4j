package org.chomookun.arch4j.core.git.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;

import jakarta.persistence.*;
import org.chomookun.arch4j.core.common.data.converter.BooleanConverter;

@Entity
@Table(name = "core_git")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GitEntity extends BaseEntity {

    @Id
    @Column(name = "git_id", length = 32)
    private String gitId;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "branch", length = 32)
    private String branch;

    @Column(name = "discussion_enabled", length = 1)
    @Convert(converter= BooleanConverter.class)
    private boolean discussionEnabled;

    @Column(name = "discussion_id", length = 32)
    private String discussionId;

    @Column(name = "note", length = 4000)
    private String note;

}
