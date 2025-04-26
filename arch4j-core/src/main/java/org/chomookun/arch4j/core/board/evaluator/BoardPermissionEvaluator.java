package org.chomookun.arch4j.core.board.evaluator;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.board.model.Board;
import org.chomookun.arch4j.core.board.BoardService;
import org.chomookun.arch4j.core.security.support.SecurityUtils;
import org.springframework.stereotype.Component;

@Component("boardPermissionEvaluator")
@RequiredArgsConstructor
public class BoardPermissionEvaluator {

    private final BoardService boardService;

    public boolean hasAccessPermission(Board board) {
        return SecurityUtils.hasPermission(board.getAccessRoles());
    }

    public boolean hasAccessPermission(String boardId) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        return hasAccessPermission(board);
    }

    public boolean hasReadPermission(Board board) {
        return SecurityUtils.hasPermission(board.getReadRoles());
    }

    public boolean hasReadPermission(String boardId) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        return hasReadPermission(board);
    }

    public boolean hasWritePermission(Board board) {
        return SecurityUtils.hasPermission(board.getWriteRoles());
    }

    public boolean hasWritePermission(String boardId) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        return hasWritePermission(board);
    }

}
