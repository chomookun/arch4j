package org.oopscraft.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.repository.ArticleEntity;
import org.oopscraft.arch4j.core.board.repository.ArticleRepository;
import org.oopscraft.arch4j.core.board.repository.ArticleVoteEntity;
import org.oopscraft.arch4j.core.board.repository.ArticleVoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleVoteService {

    private final ArticleRepository articleRepository;

    private final ArticleVoteRepository articleVoteRepository;

    /**
     * get article votes
     * @param articleId article id
     * @return votes
     */
    public List<ArticleVote> getArticleVotes(String articleId) {
        return articleVoteRepository.findAllByArticleId(articleId).stream()
                .map(ArticleVote::from)
                .collect(Collectors.toList());
    }

    /**
     * get article vote
     * @param articleId article id
     * @param userId user id
     * @return article vote
     */
    public Optional<ArticleVote> getArticleVote(String articleId, String userId) {
        ArticleVoteEntity.Pk pk = ArticleVoteEntity.Pk.builder()
                .articleId(articleId)
                .userId(userId)
                .build();
        return articleVoteRepository.findById(pk)
                .map(ArticleVote::from);
    }

    /**
     * save article vote
     * @param articleVote article vote
     */
    @Transactional
    public ArticleVote saveArticleVote(ArticleVote articleVote) {

        // get article
        ArticleEntity articleEntity = articleRepository.findById(articleVote.getArticleId()).orElseThrow();

        // update article like entity
        ArticleVoteEntity.Pk pk = ArticleVoteEntity.Pk.builder()
                .articleId(articleVote.getArticleId())
                .userId(articleVote.getUserId())
                .build();
        ArticleVoteEntity articleVoteEntity = articleVoteRepository.findById(pk).orElse(null);
        if(articleVoteEntity == null) {
            articleVoteEntity = ArticleVoteEntity.builder()
                    .articleId(articleVote.getArticleId())
                    .userId(articleVote.getUserId())
                    .build();
        }
        articleVoteEntity.setPoint(articleVote.getPoint());
        articleVoteEntity = articleVoteRepository.saveAndFlush(articleVoteEntity);

        // update article vote count
        AtomicLong votePositiveCount = new AtomicLong(0L);
        AtomicLong voteNegativeCount = new AtomicLong(0L);
        articleVoteRepository.findAllByArticleId(articleEntity.getArticleId()).forEach(element -> {
            if(element.getPoint() > 0) {
                votePositiveCount.getAndIncrement();
            }else if(element.getPoint() < 0) {
                voteNegativeCount.getAndIncrement();
            }
        });
        articleEntity.setVotePositiveCount(votePositiveCount.get());
        articleEntity.setVoteNegativeCount(voteNegativeCount.get());
        articleRepository.saveAndFlush(articleEntity);

        // return
        return ArticleVote.from(articleVoteEntity);
    }

}
