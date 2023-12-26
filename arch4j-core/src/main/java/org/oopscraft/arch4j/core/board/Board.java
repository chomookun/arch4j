package org.oopscraft.arch4j.core.board;

import lombok.*;
import org.oopscraft.arch4j.core.board.dao.BoardEntity;
import org.oopscraft.arch4j.core.board.dao.BoardRoleEntity;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.security.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Board {

    private String boardId;

    private String boardName;

    private String icon;

    private MessageFormat messageFormat;

    private String message;

    private String skin;

    private Integer pageSize;

    @Builder.Default
    private List<Role> accessRoles = new ArrayList<>();

    @Builder.Default
    private List<Role> readRoles = new ArrayList<>();

    @Builder.Default
    private List<Role> writeRoles = new ArrayList<>();

    private boolean fileEnabled;

    private Integer fileSizeLimit;

    @Builder.Default
    private List<Role> fileRoles = new ArrayList<>();

    private boolean commentEnabled;

    @Builder.Default
    private List<Role> commentRoles = new ArrayList<>();

    public boolean hasAccessPermission() {
        return SecurityUtils.hasPermission(accessRoles);
    }

    public boolean hasReadPermission() {
        return SecurityUtils.hasPermission(readRoles);
    }

    public boolean hasWritePermission() {
        return SecurityUtils.hasPermission(writeRoles);
    }

    public boolean hasFilePermission() {
        return SecurityUtils.hasPermission(fileRoles);
    }

    public boolean hasCommentPermission() {
        return SecurityUtils.hasPermission(commentRoles);
    }

    public static Board from(BoardEntity boardEntity) {
        Board board = Board.builder()
                .boardId(boardEntity.getBoardId())
                .boardName(boardEntity.getBoardName())
                .icon(boardEntity.getIcon())
                .messageFormat(boardEntity.getMessageFormat())
                .message(boardEntity.getMessage())
                .skin(boardEntity.getSkin())
                .pageSize(boardEntity.getPageSize())
                .build();

        // access policy
        List<Role> accessRoles = boardEntity.getAccessBoardRoleEntities().stream()
                .map(BoardRoleEntity::getRoleEntity)
                .filter(Objects::nonNull)
                .map(Role::from)
                .collect(Collectors.toList());
        board.setAccessRoles(accessRoles);

        // read policy
        List<Role> readRoles = boardEntity.getReadBoardRoleEntities().stream()
                .map(BoardRoleEntity::getRoleEntity)
                .filter(Objects::nonNull)
                .map(Role::from)
                .collect(Collectors.toList());
        board.setReadRoles(readRoles);

        // write policy
        List<Role> writeRoles = boardEntity.getWriteBoardRoleEntities().stream()
                .map(BoardRoleEntity::getRoleEntity)
                .filter(Objects::nonNull)
                .map(Role::from)
                .collect(Collectors.toList());
        board.setWriteRoles(writeRoles);

        // file
        board.setFileEnabled(boardEntity.isFileEnabled());
        board.setFileSizeLimit(boardEntity.getFileSizeLimit());
        List<Role> fileRoles = boardEntity.getFileBoardRoleEntities().stream()
                .map(BoardRoleEntity::getRoleEntity)
                .filter(Objects::nonNull)
                .map(Role::from)
                .collect(Collectors.toList());
        board.setFileRoles(fileRoles);

        // comment
        board.setCommentEnabled(boardEntity.isCommentEnabled());
        List<Role> commentRoles = boardEntity.getCommentBoardRoleEntities().stream()
                .map(BoardRoleEntity::getRoleEntity)
                .filter(Objects::nonNull)
                .map(Role::from)
                .collect(Collectors.toList());
        board.setCommentRoles(commentRoles);

        // return
        return board;
    }

}
