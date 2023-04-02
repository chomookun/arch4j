package org.oopscraft.arch4j.core.user;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.user.repository.RoleEntity;
import org.oopscraft.arch4j.core.user.repository.UserEntity;
import org.oopscraft.arch4j.core.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void saveUser(User user) {
        UserEntity userEntity = userRepository.findById(user.getId()).orElse(null);
        if(userEntity == null) {
            userEntity = UserEntity.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .build();
        }
        userEntity.toBuilder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .type(user.getType())
                .status(user.getStatus())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .photo(user.getPhoto())
                .profile(user.getProfile())
                .roles(user.getRoles().stream()
                        .map(role -> {
                            return RoleEntity.builder()
                                    .id(role.getId())
                                    .name(role.getName())
                                    .icon(role.getIcon())
                                    .note(role.getNote())
                                    .build();
                        })
                        .collect(Collectors.toList()))
                .build();
        userRepository.saveAndFlush(userEntity);
    }

    /**
     * find users
     * @param userSearch search condition
     * @param pageable pageable
     * @return users
     */
    public Page<User> getUsers(UserSearch userSearch, Pageable pageable) {
        Page<UserEntity> userEntityPage = userRepository.findUsers(userSearch, pageable);
        List<User> users = userEntityPage.getContent().stream()
                .map(User::from)
                .collect(Collectors.toList());
        long total = userEntityPage.getTotalElements();
        return new PageImpl<>(users, pageable, total);
    }

    /**
     * get user
     * @param id id
     * @return user
     */
    public Optional<User> getUser(String id) {
        return userRepository.findById(id).map(User::from);
    }

    /**
     * delete user
     * @param id user id
     */
    public void deleteUser(String id) {
        userRepository.deleteById(id);
        userRepository.flush();
    }

}
