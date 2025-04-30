package org.chomookun.arch4j.core.board.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.security.entity.RoleEntity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_board_role")
@IdClass(BoardRoleEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardRoleEntity extends BaseEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String boardId;
        private String roleId;
        private String type;
    }

    @Id
    @Column(name = "board_id")
    private String boardId;

    @Id
    @Column(name = "role_id")
    private String roleId;

    @Id
    @Column(name = "type", length = 16)
    private String type;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", insertable = false, updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private RoleEntity roleEntity;


}
