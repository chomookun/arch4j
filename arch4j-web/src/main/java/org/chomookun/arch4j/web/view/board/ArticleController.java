package org.chomookun.arch4j.web.view.board;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.board.model.Article;
import org.chomookun.arch4j.core.board.ArticleService;
import org.chomookun.arch4j.core.board.model.Board;
import org.chomookun.arch4j.core.board.BoardService;
import org.chomookun.arch4j.core.security.support.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/board/{boardId}/article")
@RequiredArgsConstructor
public class ArticleController {

    private final BoardService boardService;

    private final ArticleService articleService;

    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ModelAndView index(
            @PathVariable("boardId")String boardId,
            @RequestParam("articleId")String articleId
    ) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        Article article = articleService.getArticle(articleId).orElseThrow();
        ModelAndView modelAndView = new ModelAndView("board/article");
        modelAndView.addObject("_title", article.getTitle());
        modelAndView.addObject("board", board);
        modelAndView.addObject("article", article);
        return modelAndView;
    }

    @GetMapping("edit")
    public ModelAndView edit(
            @PathVariable("boardId") String boardId,
            @RequestParam(value = "articleId", required = false) String articleId
    ) {
        ModelAndView modelAndView = new ModelAndView("board/article-edit");
        Board board = boardService.getBoard(boardId).orElseThrow();
        modelAndView.addObject("board", board);
        return modelAndView;
    }














//    @RequestMapping(value = "edit", method = {RequestMethod.GET, RequestMethod.POST})
//    @PreAuthorize("@boardPermissionEvaluator.hasWritePermission(#boardId)")
//    public ModelAndView index(
//            @PathVariable("boardId") String boardId,
//            @RequestParam(value = "articleId", required = false) String articleId,
//            @RequestParam(value = "password", required = false) String password
//    ){
//        ModelAndView modelAndView = new ModelAndView("board/article-write.html");
//        // get board info
//        Board board = boardService.getBoard(boardId).orElseThrow();
//        modelAndView.addObject("board", board);
//        // when edit article
//        if(articleId != null) {
//            // get article
//            Article article = articleService.getArticle(articleId).orElseThrow();
//            modelAndView.addObject("article", article);
//            // when writer is anonymous and password is defined.
//            if(article.getUserId() == null) {
//                // password not found
//                if(password == null) {
//                    modelAndView.setViewName("board/check-password.html");
//                    return modelAndView;
//                }
//                //check password
//                if(!passwordEncoder.matches(password, article.getPassword())) {
//                    throw new RuntimeException("password not matches");
//                }
//                // append password
//                modelAndView.addObject("articlePassword", password);
//            }
//            // writer is authenticated user, check same with current user
//            else{
//                // check authenticated
//                if(!SecurityUtils.isAuthenticated()) {
//                    throw new RuntimeException("not authenticated");
//                }
//                // check article writer
//                if(!article.getUserId().equals(SecurityUtils.getCurrentUserId())){
//                    throw new RuntimeException("not writer");
//                }
//            }
//        }
//        // return
//        return modelAndView;
//    }


}
