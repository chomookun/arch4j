package org.chomookun.arch4j.core.security;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.data.ValidationUtil;
import org.chomookun.arch4j.core.security.model.Authority;
import org.chomookun.arch4j.core.security.model.AuthoritySearch;
import org.chomookun.arch4j.core.security.entity.AuthorityEntity;
import org.chomookun.arch4j.core.security.repository.AuthorityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorityService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final AuthorityRepository authorityRepository;

    /**
     * Saves authority
     * @param authority authority
     * @return saved authority
     */
    @Transactional
    public Authority saveAuthority(Authority authority) {
        ValidationUtil.validate(authority);
        AuthorityEntity authorityEntity = authorityRepository.findById(authority.getAuthorityId()).orElse(
                AuthorityEntity.builder()
                    .authorityId(authority.getAuthorityId())
                    .build());
        authorityEntity.setSystemUpdatedAt(LocalDateTime.now());
        authorityEntity.setName(authority.getName());
        authorityEntity.setNote(authority.getNote());
        // save
        AuthorityEntity savedAuthorityEntity = authorityRepository.saveAndFlush(authorityEntity);
        entityManager.refresh(savedAuthorityEntity);
        return Authority.from(savedAuthorityEntity);
    }

    /**
     * Gets authority
     * @param authorityId authority id
     * @return authority
     */
    public Optional<Authority> getAuthority(String authorityId) {
        return authorityRepository.findById(authorityId)
                .map(Authority::from);
    }

    /**
     * Deletes authority
     * @param authorityId authority id
     */
    public void deleteAuthority(String authorityId) {
        authorityRepository.deleteById(authorityId);
        authorityRepository.flush();
    }

    /**
     * Gets all authorities
     * @return authorities
     */
    public List<Authority> getAuthorities() {
        return authorityRepository.findAll().stream()
                .map(Authority::from)
                .collect(Collectors.toList());
    }

    /**
     * Gets authority page by authority search
     * @param authoritySearch authority search
     * @param pageable pageable
     * @return page of authorities
     */
    public Page<Authority> getAuthorities(AuthoritySearch authoritySearch, Pageable pageable) {
        Page<AuthorityEntity> authorityEntityPage = authorityRepository.findAll(authoritySearch, pageable);
        List<Authority> authorities = authorityEntityPage.getContent().stream()
                .map(Authority::from)
                .collect(Collectors.toList());
        return new PageImpl<>(authorities, pageable, authorityEntityPage.getTotalElements());
    }

}
