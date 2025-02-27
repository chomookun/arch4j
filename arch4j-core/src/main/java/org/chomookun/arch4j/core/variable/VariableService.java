package org.chomookun.arch4j.core.variable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.common.pbe.PbeStringUtil;
import org.chomookun.arch4j.core.variable.entity.VariableEntity;
import org.chomookun.arch4j.core.variable.repository.VariableRepository;
import org.chomookun.arch4j.core.variable.model.Variable;
import org.chomookun.arch4j.core.variable.model.VariableSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VariableService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final VariableRepository variableRepository;

    /**
     * Saves variable
     * @param variable variable
     * @return saved variable
     */
    @Transactional
    public Variable saveVariable(Variable variable) {
        VariableEntity variableEntity = variableRepository.findById(variable.getVariableId())
                .orElse(VariableEntity.builder()
                    .variableId(variable.getVariableId())
                    .build());
        variableEntity.setSystemUpdatedAt(LocalDateTime.now()); // disable dirty checking
        variableEntity.setValue(PbeStringUtil.encode(variable.getValue()));
        variableEntity.setName(variable.getName());
        variableEntity.setNote(variable.getNote());
        // saves
        VariableEntity savedVariableEntity = variableRepository.saveAndFlush(variableEntity);
        entityManager.refresh(savedVariableEntity);
        return Variable.from(savedVariableEntity);
    }

    /**
     * Gets variable
     * @param variableId variable id
     * @return variable
     */
    public Optional<Variable> getVariable(String variableId) {
        return variableRepository.findById(variableId).map(Variable::from);
    }

    /**
     * Deletes variable
     * @param variableId variable id
     */
    @Transactional
    public void deleteVariable(String variableId) {
        variableRepository.deleteById(variableId);
        variableRepository.flush();
    }

    /**
     * Gets variables
     * @param variableSearch variable search
     * @param pageable pageable
     * @return page of variable
     */
    public Page<Variable> getVariables(VariableSearch variableSearch, Pageable pageable) {
        Page<VariableEntity> variableEntityPage = variableRepository.findAll(variableSearch, pageable);
        List<Variable> properties = variableEntityPage.getContent().stream()
                .map(Variable::from)
                .collect(Collectors.toList());
        return new PageImpl<>(properties, pageable, variableEntityPage.getTotalElements());
    }

}
