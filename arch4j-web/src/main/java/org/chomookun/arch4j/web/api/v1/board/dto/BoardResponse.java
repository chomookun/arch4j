package org.chomookun.arch4j.web.api.v1.board.dto;

import lombok.*;
import org.chomookun.arch4j.core.board.model.Board;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardResponse {

    private String id;

    private String name;

    private String icon;

    private Board.MessageFormat messageFormat;

    private String message;

    private String skin;

    private Integer pageSize;

    private boolean fileEnabled;

    private boolean commentEnabled;

    private boolean hasAccessPermission;

    private boolean hasReadPermission;

    private boolean hasWritePermission;

    private boolean hasFilePermission;

    private boolean hasCommentPermission;

    public static BoardResponse from(Board board) {
        return BoardResponse.builder()
                .id(board.getBoardId())
                .name(board.getName())
                .icon(board.getIcon())
                .messageFormat(board.getMessageFormat())
                .message(board.getMessage())
                .skin(board.getSkin())
                .pageSize(board.getPageSize())
                .fileEnabled(board.isFileEnabled())
                .build();
    }

}
