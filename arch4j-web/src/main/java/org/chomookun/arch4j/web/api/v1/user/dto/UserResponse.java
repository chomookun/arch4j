package org.chomookun.arch4j.web.api.v1.user.dto;

import lombok.*;
import org.chomookun.arch4j.core.user.model.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {

    private String userId;

    private String name;

    private String email;

    private String mobile;

    private String photo;

    private String profile;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .photo(user.getIcon())
                .profile(user.getBio())
                .build();
    }

}
