package org.chomookun.arch4j.core.security.repository;

import org.chomookun.arch4j.core.security.entity.UserMfaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMfaRepository extends JpaRepository<UserMfaEntity, String> {

}
