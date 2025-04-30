package org.chomookun.arch4j.web.view.board;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.board.model.Article;
import org.chomookun.arch4j.core.board.ArticleService;
import org.chomookun.arch4j.core.board.model.Board;
import org.chomookun.arch4j.core.board.BoardService;
import org.chomookun.arch4j.core.discussion.DiscussionService;
import org.chomookun.arch4j.core.discussion.model.Discussion;
import org.chomookun.arch4j.core.discussion.provider.DiscussionProvider;
import org.chomookun.arch4j.core.discussion.provider.DiscussionProviderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/board/{boardId}/article")
@RequiredArgsConstructor
public class ArticleController {

    private final BoardService boardService;

    private final ArticleService articleService;

    private final DiscussionService discussionService;

    @GetMapping
    public ModelAndView index(
            @PathVariable("boardId")String boardId,
            @RequestParam("articleId")String articleId
    ) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        Article article = articleService.getArticle(articleId).orElseThrow();
        ModelAndView modelAndView = new ModelAndView("board/article");
        modelAndView.addObject("boardId", boardId);
        modelAndView.addObject("articleId", articleId);
        modelAndView.addObject("_title", article.getTitle());
        modelAndView.addObject("board", board);
        modelAndView.addObject("article", article);
        // discussion
        if (board.isDiscussionEnabled()) {
            Discussion discussion = discussionService.getDiscussion(board.getDiscussionId()).orElseThrow();
            modelAndView.addObject("discussion", discussion);
            DiscussionProvider discussionProvider = DiscussionProviderFactory.getDiscussionProvider(discussion);
            modelAndView.addObject("discussionProvider", discussionProvider);
            String commentTarget = String.format("board.article:%s", articleId);
            modelAndView.addObject("commentTarget", commentTarget);
        }
        // returns
        return modelAndView;
    }

    @GetMapping("edit")
    public ModelAndView edit(
            @PathVariable("boardId") String boardId,
            @RequestParam(value = "articleId", required = false) String articleId
    ) {
        ModelAndView modelAndView = new ModelAndView("board/article-edit");
        articleId = articleId == null ? articleService.generateArticleId() : articleId; // new article (draft id)
        modelAndView.addObject("boardId", boardId);
        modelAndView.addObject("articleId", articleId);
        Board board = boardService.getBoard(boardId).orElseThrow();
        modelAndView.addObject("board", board);
        return modelAndView;
    }

}
