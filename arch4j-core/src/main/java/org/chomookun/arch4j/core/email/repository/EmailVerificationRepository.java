package org.chomookun.arch4j.core.email.repository;

import org.chomookun.arch4j.core.email.entity.EmailVerificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerificationEntity, String> {

}
