package org.chomookun.arch4j.core.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.common.data.ValidationUtil;
import org.chomookun.arch4j.core.security.model.Role;
import org.chomookun.arch4j.core.user.entity.UserEntity;
import org.chomookun.arch4j.core.user.model.UserCredential;
import org.chomookun.arch4j.core.user.repository.UserRepository;
import org.chomookun.arch4j.core.user.entity.UserRoleEntity;
import org.chomookun.arch4j.core.user.model.User;
import org.chomookun.arch4j.core.user.model.UserSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final UserRepository userRepository;

    private final UserCredentialService userCredentialService;

    /**
     * Saves user
     * @param user user
     * @return saved user
     */
    @Transactional
    public User saveUser(User user) {
        ValidationUtil.validate(user);
        UserEntity userEntity;
        if (user.getUserId() != null) {
            userEntity = userRepository.findById(user.getUserId()).orElseThrow();
        } else {
            userEntity = UserEntity.builder()
                    .userId(IdGenerator.uuid())
                    .joinAt(Instant.now())
                    .passwordAt(Instant.now())
                    .build();
        }
        userEntity.setSystemUpdatedAt(LocalDateTime.now()); // disable dirty checking
        userEntity.setName(user.getName());
        userEntity.setUsername(user.getUsername());
        userEntity.setAdmin(user.isAdmin());
        userEntity.setStatus(user.getStatus());
        userEntity.setEmail(user.getEmail());
        userEntity.setMobile(user.getMobile());
        userEntity.setIcon(user.getIcon());
        userEntity.setBio(user.getBio());
        userEntity.setExpireAt(user.getExpireAt());
        //roles
        userEntity.getUserRoles().clear();
        for(Role role : user.getRoles()) {
            UserRoleEntity userRoleEntity = UserRoleEntity.builder()
                    .userId(userEntity.getUserId())
                    .roleId(role.getRoleId())
                    .build();
            userEntity.getUserRoles().add(userRoleEntity);
        }
        UserEntity savedUserEntity = userRepository.saveAndFlush(userEntity);

        // creates password credential
        if (user.getUserId() == null) {
            UserCredential passwordCredential = UserCredential.builder()
                    .userId(savedUserEntity.getUserId())
                    .type(UserCredential.Type.PASSWORD)
                    .credential(userCredentialService.generatePasswordCredential(user.getPassword()))
                    .changedAt(Instant.now())
                    .build();
            userCredentialService.saveCredential(passwordCredential);
        }

        // returns
        entityManager.refresh(savedUserEntity);
        return User.from(savedUserEntity);
    }

    /**
     * Gets user
     * @param userId user id
     * @return user
     */
    public Optional<User> getUser(String userId) {
        return userRepository.findById(userId)
                .map(User::from);
    }

    /**
     * Gets user by username
     * @param username username
     * @return user
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::from);
    }

    /**
     * Gets users
     * @param userSearch user search
     * @param pageable pageable
     * @return users
     */
    public Page<User> getUsers(UserSearch userSearch, Pageable pageable) {
        Page<UserEntity> userEntityPage = userRepository.findAll(userSearch, pageable);
        List<User> users = userEntityPage.getContent().stream()
                .map(User::from)
                .collect(Collectors.toList());
        return new PageImpl<>(users, pageable, userEntityPage.getTotalElements());
    }

    /**
     * Deletes user
     * @param userId user id
     */
    public void deleteUser(String userId) {
        userCredentialService.deleteCredentials(userId);
        UserEntity userEntity = userRepository.findById(userId).orElseThrow();
        userRepository.delete(userEntity);
        userRepository.flush();
    }

}
