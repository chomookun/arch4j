package org.chomookun.arch4j.core.variable;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chomookun.arch4j.core.variable.entity.VariableEntity;
import org.chomookun.arch4j.core.variable.model.Variable;
import org.chomookun.arch4j.core.variable.model.VariableSearch;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RequiredArgsConstructor
class VariableServiceTest extends CoreTestSupport {

    final VariableService variableService;

    @Test
    void saveVariableForPersist() {
        // given
        Variable variable = Variable.builder()
                .variableId("test.name")
                .name("test variable")
                .build();
        // when
        Variable savedVariable = variableService.saveVariable(variable);
        // then
        assertNotNull(entityManager.find(VariableEntity.class, savedVariable.getVariableId()));
    }

    @Test
    void saveVariableForMerge() {
        // given
        VariableEntity variableEntity = VariableEntity.builder()
                .variableId("test.name")
                .name("test variable")
                .build();
        entityManager.persist(variableEntity);
        entityManager.flush();
        // when
        entityManager.refresh(variableEntity);
        Variable variable = Variable.from(variableEntity);
        variable.setName("changed");
        Variable savedVariable = variableService.saveVariable(variable);
        // then
        assertEquals("changed", entityManager.find(VariableEntity.class, savedVariable.getVariableId()).getName());
    }

    @Test
    void getMenu() {
        // given
        VariableEntity variableEntity = VariableEntity.builder()
                .variableId("test.name")
                .name("test variable")
                .build();
        entityManager.persist(variableEntity);
        entityManager.flush();
        // when
        Variable variable = variableService.getVariable(variableEntity.getVariableId()).orElse(null);
        // then
        assertNotNull(variable);
    }

    @Test
    void deleteVariable() {
        // given
        VariableEntity variableEntity = VariableEntity.builder()
                .variableId("test.name")
                .name("test variable")
                .build();
        entityManager.persist(variableEntity);
        entityManager.flush();
        // when
        variableService.deleteVariable(variableEntity.getVariableId());
        // then
        assertNull(entityManager.find(VariableEntity.class, variableEntity.getVariableId()));
    }

    @Test
    void getVariables() {
        // given
        VariableEntity variableEntity = VariableEntity.builder()
                .variableId("test.name")
                .name("test variable")
                .build();
        entityManager.persist(variableEntity);
        entityManager.flush();
        VariableSearch variableSearch = VariableSearch.builder()
                .variableId("test.name")
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        // when
        List<Variable> variables = variableService.getVariables(variableSearch, pageable).getContent();
        // then
        assertTrue(variables.stream().allMatch(e -> e.getVariableId().contains(variableSearch.getVariableId())));
    }

}
