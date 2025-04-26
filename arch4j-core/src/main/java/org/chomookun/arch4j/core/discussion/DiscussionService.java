package org.chomookun.arch4j.core.discussion;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.discussion.entity.DiscussionEntity;
import org.chomookun.arch4j.core.discussion.model.Discussion;
import org.chomookun.arch4j.core.discussion.model.DiscussionSearch;
import org.chomookun.arch4j.core.discussion.repository.DiscussionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscussionService {

    private final DiscussionRepository discussionRepository;

    public Discussion saveDiscussion(Discussion discussion) {
        DiscussionEntity discussionEntity;
        if (discussion.getDiscussionId() == null) {
            discussionEntity = DiscussionEntity.builder()
                    .discussionId(IdGenerator.uuid())
                    .build();
        } else {
            discussionEntity = discussionRepository.findById(discussion.getDiscussionId()).orElseThrow();
        }
        discussionEntity.setName(discussion.getName());
        discussionEntity.setDiscussionProviderId(discussion.getDiscussionProviderId());
        discussionEntity.setDiscussionProviderConfig(discussion.getDiscussionProviderConfig());
        DiscussionEntity savedDiscussionEntity = discussionRepository.saveAndFlush(discussionEntity);
        return Discussion.from(savedDiscussionEntity);
    }

    public Page<Discussion> getDiscussions(DiscussionSearch discussionSearch, Pageable pageable) {
        Page<DiscussionEntity> discussionEntityPage = discussionRepository.findAll(discussionSearch, pageable);
        List<Discussion> discussions = discussionEntityPage.getContent().stream()
                .map(Discussion::from)
                .collect(Collectors.toList());
        long total = discussionEntityPage.getTotalElements();
        return new PageImpl<>(discussions, pageable, total);
    }

    public Optional<Discussion> getDiscussion(String discussionId) {
        return discussionRepository.findById(discussionId)
                .map(Discussion::from);
    }

    public void deleteDiscussion(String discussionId) {
        discussionRepository.deleteById(discussionId);
        discussionRepository.flush();
    }

}
