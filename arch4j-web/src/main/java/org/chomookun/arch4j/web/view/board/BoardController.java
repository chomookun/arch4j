package org.chomookun.arch4j.web.view.board;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.board.model.Board;
import org.chomookun.arch4j.core.board.BoardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/board/{boardId}")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
//    @PreAuthorize("@boardPermissionEvaluator.hasAccessPermission(#boardId)")
    public ModelAndView index(@PathVariable("boardId")String boardId) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        ModelAndView modelAndView = new ModelAndView("board/board");
        modelAndView.addObject("boardId", boardId);
        modelAndView.addObject("_title", board.getName());
        modelAndView.addObject("board", board);
        return modelAndView;
    }

}
