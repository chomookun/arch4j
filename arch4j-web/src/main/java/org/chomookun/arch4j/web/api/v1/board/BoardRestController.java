package org.chomookun.arch4j.web.api.v1.board;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.board.BoardService;
import org.chomookun.arch4j.web.api.v1.board.dto.BoardResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "board")
@RestController
@RequestMapping("api/v1/boards")
@RequiredArgsConstructor
public class BoardRestController {

    private final BoardService boardService;

    @GetMapping("{boardId}")
//    @PreAuthorize("@boardPermissionEvaluator.hasAccessPermission(#boardId)")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable("boardId") String boardId) {
        BoardResponse boardResponse = boardService.getBoard(boardId)
                .map(BoardResponse::from)
                .orElseThrow();
        return ResponseEntity.ok(boardResponse);
    }

}
