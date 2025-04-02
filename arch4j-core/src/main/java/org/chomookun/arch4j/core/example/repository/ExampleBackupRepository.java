package org.chomookun.arch4j.core.example.repository;

import org.chomookun.arch4j.core.example.entity.ExampleBackupEntity;
import org.chomookun.arch4j.core.example.entity.ExampleEntity;
import org.chomookun.arch4j.core.example.entity.ExampleEntity_;
import org.chomookun.arch4j.core.example.model.ExampleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Example Backup Repository
 */
@Repository
public interface ExampleBackupRepository extends JpaRepository<ExampleBackupEntity, String>, JpaSpecificationExecutor<ExampleBackupEntity> {

}
