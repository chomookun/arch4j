package org.chomookun.arch4j.web.view.board;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.board.model.Article;
import org.chomookun.arch4j.core.board.ArticleService;
import org.chomookun.arch4j.core.board.model.Board;
import org.chomookun.arch4j.core.board.BoardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/board/{boardId}/article-read")
@RequiredArgsConstructor
public class ArticleReadController {

    private final BoardService boardService;

    private final ArticleService articleService;

    @GetMapping
    @PreAuthorize("@boardPermissionEvaluator.hasReadPermission(#boardId)")
    public ModelAndView index(
            @PathVariable("boardId")String boardId,
            @RequestParam("articleId")String articleId
    ) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        Article article = articleService.getArticle(articleId).orElseThrow();
        ModelAndView modelAndView = new ModelAndView("board/article-read.html");
        modelAndView.addObject("_title", article.getTitle());
        modelAndView.addObject("board", board);
        modelAndView.addObject("article", article);
        return modelAndView;
    }

}
