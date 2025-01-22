package org.chomookun.arch4j.web.board.api.v1;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.board.model.ArticleVote;
import org.chomookun.arch4j.core.board.service.ArticleVoteService;
import org.chomookun.arch4j.web.board.api.v1.dto.ArticleVoteRequest;
import org.chomookun.arch4j.web.board.api.v1.dto.ArticleVoteResponse;
import org.chomookun.arch4j.web.security.support.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("api/v1/board/{boardId}/article/{articleId}/vote")
@RequiredArgsConstructor
@Tag(name = "board")
public class ArticleVoteRestController {

    private final ArticleVoteService articleVoteService;

    @GetMapping
    public ResponseEntity<ArticleVoteResponse> getArticleVote(
            @PathVariable("boardId") String boardId,
            @PathVariable("articleId") String articleId
    ) {
        // get article vote
        String userId = SecurityUtils.getCurrentUserId();
        ArticleVoteResponse articleVoteResponse = articleVoteService.getArticleVote(articleId, userId)
                .map(ArticleVoteResponse::from)
                .orElse(ArticleVoteResponse.builder()
                        .articleId(articleId)
                        .point(0L)
                        .build());

        // calculate count
        AtomicReference<Long> positiveCount = new AtomicReference<>(0L);
        AtomicReference<Long> negativeCount = new AtomicReference<>(0L);
        articleVoteService.getArticleVotes(articleId).forEach(articleVote -> {
            if(articleVote.getPoint() > 0) {
                positiveCount.getAndSet(positiveCount.get() + 1);
            }
            if(articleVote.getPoint() < 0) {
                negativeCount.getAndSet(negativeCount.get() + 1);
            }
        });
        articleVoteResponse.setPositiveCount(positiveCount.get());
        articleVoteResponse.setNegativeCount(negativeCount.get());

        // response
        return ResponseEntity.ok()
                .body(articleVoteResponse);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Void> saveArticleVote(
            @PathVariable("boardId") String boardId,
            @PathVariable("articleId") String articleId,
            @RequestBody ArticleVoteRequest articleVoteRequest
    ) {
        // check authenticated
        SecurityUtils.checkAuthenticated();

        // save
        ArticleVote articleVote = ArticleVote.builder()
                .articleId(articleId)
                .userId(SecurityUtils.getCurrentUserId())
                .point(articleVoteRequest.getPoint())
                .build();
        articleVoteService.saveArticleVote(articleVote);

        // response
        return ResponseEntity.ok().build();
    }

}
