package org.chomoo.arch4j.web.board.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.chomoo.arch4j.core.board.service.BoardService;
import org.chomoo.arch4j.web.board.api.v1.dto.BoardResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/board")
@RequiredArgsConstructor
@Tag(name = "board", description = "Board")
public class BoardRestController {

    private final BoardService boardService;

    @GetMapping("{boardId}")
    @PreAuthorize("@boardPermissionEvaluator.hasAccessPermission(#boardId)")
    @Operation(summary = "get board info", description = "returns board information")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable("boardId") String boardId) {
        BoardResponse boardResponse = boardService.getBoard(boardId)
                .map(BoardResponse::from)
                .orElseThrow();
        return ResponseEntity.ok(boardResponse);
    }

}
