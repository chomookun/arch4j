package org.oopscraft.arch4j.core.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistoryEntity, LoginHistoryEntity.Pk>, JpaSpecificationExecutor<LoginHistoryEntity> {


}
