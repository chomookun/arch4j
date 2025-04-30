package org.chomookun.arch4j.core.user.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSearch {

    private String username;

    private String name;

    private String email;

    private String mobile;

    private Boolean admin;

    private User.Status status;

}
