package org.oopscraft.arch4j.core.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.oopscraft.arch4j.core.CoreConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Slf4j
@SpringBootTest(
        classes = CoreConfiguration.class,
        properties = {
                "spring.main.lazy-initialization=true",
                "spring.main.web-application-type=none"
        }
)
@Import(CoreConfiguration.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@Rollback
public abstract class CoreTestSupport {

    @Autowired
    @PersistenceContext
    protected EntityManager entityManager;

}
