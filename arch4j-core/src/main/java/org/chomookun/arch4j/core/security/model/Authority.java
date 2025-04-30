package org.chomookun.arch4j.core.security.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseModel;
import org.chomookun.arch4j.core.security.entity.AuthorityEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Authority extends BaseModel {

    @NotNull
    private String authorityId;

    @NotBlank
    private String name;

    private String description;

    /**
     * authority factory method
     * @param authorityEntity authority entity
     * @return authority
     */
    public static Authority from(AuthorityEntity authorityEntity) {
        return Authority.builder()
                .systemRequired(authorityEntity.isSystemRequired())
                .systemUpdatedAt(authorityEntity.getSystemUpdatedAt())
                .systemUpdatedBy(authorityEntity.getSystemUpdatedBy())
                .authorityId(authorityEntity.getAuthorityId())
                .name(authorityEntity.getName())
                .description(authorityEntity.getDescription())
                .build();
    }

}
