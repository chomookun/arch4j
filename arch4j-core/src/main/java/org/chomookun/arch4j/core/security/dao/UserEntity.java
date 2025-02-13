package org.chomookun.arch4j.core.security.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.data.converter.AbstractEnumConverter;
import org.chomookun.arch4j.core.common.data.converter.BooleanConverter;
import org.chomookun.arch4j.core.common.data.converter.CryptoConverter;
import org.chomookun.arch4j.core.security.model.User;

import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "core_user",
    indexes = {
        @Index(name = "ix_username", columnList = "username"),
        @Index(name = "ix_name", columnList = "name"),
        @Index(name = "ix_email", columnList = "email"),
        @Index(name = "ix_mobile", columnList = "mobile"),
    }
)
@Comment("User information")
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserEntity extends BaseEntity {

    @Id
    @Column(name = "user_id", length = 32)
    @Comment("User ID")
    @Setter(AccessLevel.PRIVATE)
    private String userId;

    @Column(name = "username", unique = true, length = 128)
    @Comment("Username")
    private String username;

    @Column(name = "password", length = 256)
    @Comment("Password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "name", length = 128)
    @Comment("Name")
    private String name;

    @Column(name = "admin", length = 1)
    @Comment("Whether admin or not")
    @Convert(converter = BooleanConverter.class)
    private boolean admin;

    @Column(name = "status", length = 16)
    @Comment("Status")
    private User.Status status;

    @Column(name = "email", unique = true, length = 128)
    @Comment("Email")
    @Convert(converter = CryptoConverter.class)
    private String email;

    @Column(name = "mobile", unique = true, length = 64)
    @Comment("Mobile number")
    @Convert(converter = CryptoConverter.class)
    private String mobile;

    @Column(name = "photo")
    @Comment("Photo")
    @Lob
    private String photo;

    @Column(name = "profile")
    @Comment("profile")
    @Lob
    private String profile;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", updatable = false)
    @Builder.Default
    private List<UserRoleEntity> userRoles = new ArrayList<>();

    @Converter(autoApply = true)
    public static class StatusConverter extends AbstractEnumConverter<User.Status> {}

}
