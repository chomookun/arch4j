package org.chomookun.arch4j.shell.common.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.chomookun.arch4j.shell.ShellConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest(
        classes = ShellConfiguration.class,
        properties = {
                "spring.main.lazy-initialization=true",
                "spring.main.web-application-type=none",
                "spring.shell.interactive.enabled=false"
        }
)
@TestPropertySource(properties = {
        "spring.h2.console.enabled=false"
})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@Rollback
public class ShellTestSupport {

    protected final void setIn(String value) {
        value = value + System.lineSeparator();
        System.setIn(new java.io.ByteArrayInputStream(value.getBytes()));
    }

}
