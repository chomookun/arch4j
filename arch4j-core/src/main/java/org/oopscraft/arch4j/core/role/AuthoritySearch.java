package org.oopscraft.arch4j.core.role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthoritySearch {

    private String authorityId;

    private String authorityName;

}
