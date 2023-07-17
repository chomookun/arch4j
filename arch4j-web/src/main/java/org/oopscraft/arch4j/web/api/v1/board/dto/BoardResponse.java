package org.oopscraft.arch4j.web.api.v1.board.dto;

import lombok.*;
import org.oopscraft.arch4j.core.board.Board;
import org.oopscraft.arch4j.core.board.MessageFormat;
import org.oopscraft.arch4j.core.security.SecurityPolicy;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardResponse {

    private String boardId;

    private String boardName;

    private String icon;

    private MessageFormat messageFormat;

    private String message;

    private String skin;

    private Integer pageSize;

    private boolean fileEnabled;

    private SecurityPolicy accessPolicy;

    private SecurityPolicy readPolicy;

    private SecurityPolicy writePolicy;

    private boolean commentEnabled;

    private SecurityPolicy commentPolicy;

    private boolean hasAccessPermission;

    private boolean hasReadPermission;

    private boolean hasWritePermission;

    private boolean hasCommentPermission;

    public static BoardResponse from(Board board) {
        return BoardResponse.builder()
                .boardId(board.getBoardId())
                .boardName(board.getBoardName())
                .icon(board.getIcon())
                .messageFormat(board.getMessageFormat())
                .message(board.getMessage())
                .skin(board.getSkin())
                .pageSize(board.getPageSize())
                .fileEnabled(board.isFileEnabled())
                .readPolicy(board.getReadPolicy())
                .writePolicy(board.getWritePolicy())
                .commentEnabled(board.isCommentEnabled())
                .commentPolicy(board.getCommentPolicy())
                // permission
                .hasReadPermission(board.hasReadPermission())
                .hasWritePermission(board.hasWritePermission())
                .hasCommentPermission(board.hasCommentPermission())
                .build();
    }

}
