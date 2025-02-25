package org.chomookun.arch4j.core.role.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleSearch {

    private String roleId;

    private String name;

}
