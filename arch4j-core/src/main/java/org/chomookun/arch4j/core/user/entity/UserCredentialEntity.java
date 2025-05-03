package org.chomookun.arch4j.core.user.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.data.converter.GenericEnumConverter;
import org.chomookun.arch4j.core.user.model.User;
import org.chomookun.arch4j.core.user.model.UserCredential;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Type;

import java.time.Instant;

@Entity
@Table(name = "core_credential")
@IdClass(UserCredentialEntity.Pk.class)
@Comment("User credentials")
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserCredentialEntity extends BaseEntity {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements java.io.Serializable {
        private String userId;
        private UserCredential.Type type;
    }

    @Id
    @Column(name = "user_id", length = 32)
    private String userId;

    @Id
    @Column(name = "type", length = 16)
    @Enumerated(EnumType.STRING)
    @Type(TypeConverter.class)  // @Convert is not work in @Id
    private UserCredential.Type type;

    @Column(name = "credential", length = Integer.MAX_VALUE)
    @Lob
    private String credential;

    @Column(name = "changed_at")
    private Instant changedAt;

    @Converter(autoApply = true)
    public static class TypeConverter extends GenericEnumConverter<UserCredential.Type> {}

}
