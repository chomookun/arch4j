package org.chomookun.arch4j.core.security.repository;

import org.chomookun.arch4j.core.security.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity>, UserRepositoryCustom {

    Optional<UserEntity> findByUsername(String username);

}
