package org.oopscraft.arch4j.core.user.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.role.repository.RoleEntity;
import org.oopscraft.arch4j.core.user.UserStatus;
import org.oopscraft.arch4j.core.user.UserType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "core_user")
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends SystemFieldEntity {

    @Id
    @Column(name = "user_id", length = 32)
    private String userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "type", nullable = false)
    @Builder.Default
    private UserType type = UserType.GENERAL;

    @Column(name = "status", nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "join_datetime")
    private LocalDateTime joinDateTime;

    @Column(name = "close_datetime")
    private LocalDateTime closeDateTime;

    @Column(name = "photo")
    @Lob
    private String photo;

    @Column(name = "profile")
    @Lob
    private String profile;

    /**
     * roles
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "core_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            foreignKey = @ForeignKey(name = "none"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            inverseForeignKey = @ForeignKey(name = "none")
    )
    @Builder.Default
    List<RoleEntity> roles = new ArrayList<>();

}
