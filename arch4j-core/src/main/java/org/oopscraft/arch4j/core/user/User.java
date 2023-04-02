package org.oopscraft.arch4j.core.user;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.user.repository.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class User {

    private final String id;

    private String name;

    private String nickname;

    private String password;

    @Builder.Default
    private UserType type = UserType.GENERAL;

    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    private String mobile;

    private String email;

    private String photo;

    private String profile;

    @Builder.Default
    List<Role> roles = new ArrayList<>();

    public static User from(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .nickname(userEntity.getNickname())
                .password(userEntity.getPassword())
                .type(userEntity.getType())
                .status(userEntity.getStatus())
                .mobile(userEntity.getMobile())
                .email(userEntity.getEmail())
                .photo(userEntity.getPhoto())
                .profile(userEntity.getProfile())
                .roles(userEntity.getRoles().stream()
                        .map(Role::from)
                        .collect(Collectors.toList()))
                .build();
    }

}
