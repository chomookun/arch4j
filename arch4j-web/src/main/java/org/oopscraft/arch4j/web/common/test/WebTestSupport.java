package org.oopscraft.arch4j.web.common.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.oopscraft.arch4j.web.WebConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;

@SpringBootTest(
        classes = WebConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@WithUserDetails(value="admin")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@Rollback
@Slf4j
public class WebTestSupport {

    @Autowired(required = false)
    private ScheduledAnnotationBeanPostProcessor scheduledAnnotationBeanPostProcessor;

    @Autowired
    @Getter
    protected WebApplicationContext webApplicationContext;

    @Autowired
    @Getter
    protected MockMvc mockMvc;

    @Autowired
    @Getter
    protected ObjectMapper objectMapper;

    @Autowired
    @Getter
    protected EntityManager entityManager;

    @BeforeEach
    void cancelScheduledTasks() {
        if(scheduledAnnotationBeanPostProcessor != null) {
            scheduledAnnotationBeanPostProcessor.getScheduledTasks()
                    .forEach(ScheduledTask::cancel);
        }
    }

}
