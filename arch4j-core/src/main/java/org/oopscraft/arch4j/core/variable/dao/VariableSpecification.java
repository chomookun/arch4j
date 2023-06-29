package org.oopscraft.arch4j.core.variable.dao;

import org.springframework.data.jpa.domain.Specification;

public class VariableSpecification {

    public static Specification<VariableEntity> likeVariableId(String variableId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(VariableEntity_.VARIABLE_ID), '%' + variableId + '%');
    }

    public static Specification<VariableEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
               criteriaBuilder.like(root.get(VariableEntity_.NAME), '%' + name + '%');
    }

}
