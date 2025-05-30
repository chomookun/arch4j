package org.chomookun.arch4j.core.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.data.converter.CryptoConverter;
import org.chomookun.arch4j.core.common.data.converter.GenericEnumConverter;
import org.chomookun.arch4j.core.common.data.converter.BooleanConverter;
import org.chomookun.arch4j.core.user.model.User;

import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "core_user",
    indexes = {
            @Index(name = "ix_username", columnList = "username"),
            @Index(name = "ix_email", columnList = "email"),
            @Index(name = "ix_mobile", columnList = "mobile"),
            @Index(name = "ix_name", columnList = "name")
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
    @Setter(AccessLevel.PRIVATE)
    @Comment("User ID")
    private String userId;

    @Column(name = "username", unique = true, nullable = false, length = 128)
    @Comment("Username")
    private String username;

    @Column(name = "password", length = 256)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // defensive
    @Comment("Password")
    private String password;

    @Column(name = "totp_secret", length = 4000)
    @Convert(converter = CryptoConverter.class)
    @Lob
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // defensive
    @Comment("TOTP Secret")
    private String totpSecret;

    @Column(name = "name", nullable = false, length = 128)
    @Comment("Name")
    private String name;

    @Column(name = "status", length = 16)
    @Comment("Status")
    private User.Status status;

    @Column(name = "admin", length = 1)
    @Convert(converter = BooleanConverter.class)
    @Comment("Whether admin or not")
    private boolean admin;

    @Column(name = "email", unique = true)
    @Comment("Email")
    private String email;

    @Column(name = "mobile", unique = true)
    @Comment("Mobile number")
    private String mobile;

    @Column(name = "mfa_enabled", length = 1)
    @Convert(converter = BooleanConverter.class)
    @Comment("Whether MFA is enabled or not")
    private boolean mfaEnabled;

    @Column(name = "icon", length = 4000)
    @Lob
    @Comment("Icon")
    private String icon;

    @Column(name = "bio", length = 4000)
    @Lob
    @Comment("Bio")
    private String bio;

    @Column(name = "join_at")
    private Instant joinAt;

    @Column(name = "close_at")
    private Instant closeAt;

    @Column(name = "password_at")
    private Instant passwordAt;

    @Column(name = "expire_at")
    private Instant expireAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", updatable = false)
    @Builder.Default
    private List<UserRoleEntity> userRoles = new ArrayList<>();

    @Converter(autoApply = true)
    public static class StatusConverter extends GenericEnumConverter<User.Status> {}

}
