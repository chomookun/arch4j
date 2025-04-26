package org.chomookun.arch4j.core.discussion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.discussion.entity.CommentEntity;
import org.chomookun.arch4j.core.discussion.model.Comment;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.junit.jupiter.api.Test;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
class CommentServiceTest extends CoreTestSupport {

    private final CommentService commentService;

    @Test
    void saveComment() {
        // when
        Comment comment = Comment.builder()
                .targetType("board.article")
                .targetId(IdGenerator.uuid())
                .content("test-content")
                .build();
        Comment savedComment = commentService.saveComment(comment);
        // then
        log.info("saved comment: {}", savedComment);
    }

    @Test
    void getComments() {
        // given
        CommentEntity commentEntity = CommentEntity.builder()
                .commentId(IdGenerator.uuid())
                .targetType("board.article")
                .targetId(IdGenerator.uuid())
                .content("test-content")
                .build();
        entityManager.persist(commentEntity);
        entityManager.flush();
        // when
        List<Comment> comments = commentService.getComments(commentEntity.getTargetType(), commentEntity.getTargetId());
        // then
        log.info("comments: {}", comments);
    }

}