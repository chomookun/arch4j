package org.chomookun.arch4j.core.board.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.board.entity.BoardEntity;
import org.chomookun.arch4j.core.board.entity.BoardRoleEntity;
import org.chomookun.arch4j.core.common.data.BaseModel;
import org.chomookun.arch4j.core.security.model.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Board extends BaseModel {

    private String boardId;

    private String name;

    private String icon;

    private String message;

    private MessageFormat messageFormat;

    private String skin;

    private Integer pageSize;

    private boolean fileEnabled;

    private String storageId;

    private Integer fileSizeLimit;

    private boolean discussionEnabled;

    private String discussionId;

    @Builder.Default
    private List<Role> accessRoles = new ArrayList<>();

    @Builder.Default
    private List<Role> readRoles = new ArrayList<>();

    @Builder.Default
    private List<Role> writeRoles = new ArrayList<>();

    public enum MessageFormat { TEXT, MARKDOWN }

    /**
     * Factory method
     * @param boardEntity board entity
     * @return board model
     */
    public static Board from(BoardEntity boardEntity) {
        Board board = Board.builder()
                .systemRequired(boardEntity.isSystemRequired())
                .systemUpdatedAt(boardEntity.getSystemUpdatedAt())
                .systemUpdatedBy(boardEntity.getSystemUpdatedBy())
                .boardId(boardEntity.getBoardId())
                .name(boardEntity.getBoardName())
                .icon(boardEntity.getIcon())
                .messageFormat(boardEntity.getMessageFormat())
                .message(boardEntity.getMessage())
                .skin(boardEntity.getSkin())
                .pageSize(boardEntity.getPageSize())
                .fileEnabled(boardEntity.isFileEnabled())
                .storageId(boardEntity.getStorageId())
                .fileSizeLimit(boardEntity.getFileSizeLimit())
                .discussionEnabled(boardEntity.isDiscussionEnabled())
                .discussionId(boardEntity.getDiscussionId())
                .build();
        // access policy
        List<Role> accessRoles = boardEntity.getBoardRoleEntities().stream()
                .map(BoardRoleEntity::getRoleEntity)
                .filter(Objects::nonNull)
                .map(Role::from)
                .collect(Collectors.toList());
        board.setAccessRoles(accessRoles);
        // read policy
        List<Role> readRoles = boardEntity.getBoardRoleEntities().stream()
                .map(BoardRoleEntity::getRoleEntity)
                .filter(Objects::nonNull)
                .map(Role::from)
                .collect(Collectors.toList());
        board.setReadRoles(readRoles);
        // write policy
        List<Role> writeRoles = boardEntity.getBoardRoleEntities().stream()
                .map(BoardRoleEntity::getRoleEntity)
                .filter(Objects::nonNull)
                .map(Role::from)
                .collect(Collectors.toList());
        board.setWriteRoles(writeRoles);
        // return
        return board;
    }

}
