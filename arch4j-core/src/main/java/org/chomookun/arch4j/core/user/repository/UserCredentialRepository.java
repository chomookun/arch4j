package org.chomookun.arch4j.core.user.repository;

import org.chomookun.arch4j.core.user.entity.UserCredentialEntity;
import org.chomookun.arch4j.core.user.model.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredentialEntity, UserCredentialEntity.Pk>, JpaSpecificationExecutor<UserCredentialEntity> {

    @Query("""
        select u from UserCredentialEntity u
        where u.userId = :userId
        and u.type = :type
    """)
    Optional<UserCredentialEntity> findByUserIdAndType(@Param("userId") String userId, @Param("type") UserCredential.Type type);

    @Modifying
    @Transactional
    @Query("""
        delete from UserCredentialEntity u
        where u.userId = :userId
    """)
    void deleteByUserId(@Param("userId") String userId);

}
