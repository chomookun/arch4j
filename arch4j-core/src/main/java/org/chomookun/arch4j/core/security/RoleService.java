package org.chomookun.arch4j.core.security;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.data.ValidationUtil;
import org.chomookun.arch4j.core.security.model.Role;
import org.chomookun.arch4j.core.security.model.RoleSearch;
import org.chomookun.arch4j.core.security.entity.RoleAuthorityEntity;
import org.chomookun.arch4j.core.security.entity.RoleEntity;
import org.chomookun.arch4j.core.security.repository.RoleRepository;
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
public class RoleService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final RoleRepository roleRepository;

    /**
     * Saves role
     * @param role role
     * @return saved role
     */
    @Transactional
    public Role saveRole(Role role) {
        ValidationUtil.validate(role);
        final RoleEntity roleEntity = roleRepository.findById(role.getRoleId())
                .orElse(RoleEntity.builder()
                    .roleId(role.getRoleId())
                    .build());
        roleEntity.setSystemUpdatedAt(LocalDateTime.now());
        roleEntity.setName(role.getName());
        roleEntity.setAnonymous(role.isAnonymous());
        roleEntity.setAuthenticated(role.isAuthenticated());
        roleEntity.setDescription(role.getDescription());
        // authorities
        roleEntity.getAuthorities().clear();
        role.getAuthorities().forEach(authority -> {
            RoleAuthorityEntity roleAuthorityEntity = RoleAuthorityEntity.builder()
                    .roleId(roleEntity.getRoleId())
                    .authorityId(authority.getAuthorityId())
                    .build();
            roleEntity.getAuthorities().add(roleAuthorityEntity);
        });
        // save
        RoleEntity savedRoleEntity = roleRepository.saveAndFlush(roleEntity);
        entityManager.refresh(savedRoleEntity);
        return Role.from(savedRoleEntity);
    }

    /**
     * Gets role
     * @param roleId role id
     * @return role
     */
    public Optional<Role> getRole(String roleId) {
        return roleRepository.findById(roleId)
                .map(Role::from);
    }

    /**
     * Deletes role
     * @param roleId role id
     */
    public void deleteRole(String roleId) {
        RoleEntity roleEntity = roleRepository.findById(roleId).orElseThrow();
        roleRepository.delete(roleEntity);
        roleRepository.flush();
    }

    /**
     * Gets all roles
     * @return all roles
     */
    public List<Role> getRoles() {
        return roleRepository.findAll().stream()
                .map(Role::from)
                .collect(Collectors.toList());
    }

    /**
     * Gets roles page
     * @param roleSearch role search
     * @param pageable pageable
     * @return page of role
     */
    public Page<Role> getRoles(RoleSearch roleSearch, Pageable pageable) {
        Page<RoleEntity> roleEntityPage = roleRepository.findAll(roleSearch, pageable);
        List<Role> roles = roleEntityPage.getContent().stream()
                .map(Role::from)
                .collect(Collectors.toList());
        return new PageImpl<>(roles, pageable, roleEntityPage.getTotalElements());
    }

}
