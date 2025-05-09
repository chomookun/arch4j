package org.chomookun.arch4j.core.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base32;
import org.chomookun.arch4j.core.common.data.IdGenerator;
import org.chomookun.arch4j.core.common.data.ValidationUtil;
import org.chomookun.arch4j.core.security.TotpService;
import org.chomookun.arch4j.core.security.model.Role;
import org.chomookun.arch4j.core.user.entity.UserEntity;
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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TotpService totpService;

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
                    .password(passwordEncoder.encode(user.getPassword()))
                    .totpSecret(totpService.generateTotpSecret())
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
        userEntity.setMfaEnabled(user.isMfaEnabled());
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
        UserEntity userEntity = userRepository.findById(userId).orElseThrow();
        userRepository.delete(userEntity);
        userRepository.flush();
    }

    /**
     * Checks if password is matched
     * @param userId user id
     * @param password password
     * @return whether password is matched or not
     */
    public boolean isPasswordMatched(String userId, String password) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow();
        return passwordEncoder.matches(password, userEntity.getPassword());
    }

    /**
     * Changes password
     * @param userId user id
     * @param newPassword new password
     */
    @Transactional
    public void changePassword(String userId, String newPassword) {
        userRepository.findById(userId).ifPresent(userEntity -> {
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userEntity.setPasswordAt(Instant.now());
            userRepository.saveAndFlush(userEntity);
        });
    }

    @Transactional
    public String createTotpSecret(String userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow();
        String totpSecret = totpService.generateTotpSecret();
        userEntity.setTotpSecret(totpSecret);
        userRepository.saveAndFlush(userEntity);
        return totpSecret;
    }

}
