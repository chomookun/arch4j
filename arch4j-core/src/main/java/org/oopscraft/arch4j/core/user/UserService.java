package org.oopscraft.arch4j.core.user;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;
import org.oopscraft.arch4j.core.user.dao.UserEntity;
import org.oopscraft.arch4j.core.user.dao.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User saveUser(User user) {
        UserEntity userEntity = userRepository.findById(user.getUserId()).orElse(
                UserEntity.builder()
                        .userId(user.getUserId())
                        .password(passwordEncoder.encode(user.getPassword()))
                        .joinAt(LocalDateTime.now())
                        .build());
        userEntity.setUserName(user.getUserName());
        userEntity.setType(user.getType());
        userEntity.setStatus(user.getStatus());
        userEntity.setEmail(user.getEmail());
        userEntity.setMobile(user.getMobile());
        userEntity.setPhoto(user.getPhoto());
        userEntity.setProfile(user.getProfile());
        userEntity.setRoles(user.getRoles().stream()
                .map(role -> RoleEntity.builder()
                            .roleId(role.getRoleId())
                            .roleName(role.getRoleName())
                            .note(role.getNote())
                            .build())
                .collect(Collectors.toList()));
        userEntity = userRepository.saveAndFlush(userEntity);
        return User.from(userEntity);
    }

    public Optional<User> getUser(String userId) {
        return userRepository.findById(userId).map(User::from);
    }

    public Optional<User> getUserByEmail(String email) {
        User user = Optional.ofNullable(userRepository.findFirstByEmail(email))
                .map(User::from)
                .orElse(null);
        return Optional.ofNullable(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
        userRepository.flush();
    }

    public Page<User> getUsers(UserSearch userSearch, Pageable pageable) {
        Page<UserEntity> userEntityPage = userRepository.findAll(userSearch, pageable);
        List<User> users = userEntityPage.getContent().stream()
                .map(User::from)
                .collect(Collectors.toList());
        return new PageImpl<>(users, pageable, userEntityPage.getTotalElements());
    }

    public boolean isPasswordMatched(String userId, String password) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow();
        return passwordEncoder.matches(password, userEntity.getPassword());
    }

    public void changePassword(String userId, String newPassword) {
        userRepository.findById(userId).ifPresent(userEntity -> {
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userRepository.saveAndFlush(userEntity);
        });
    }

}
