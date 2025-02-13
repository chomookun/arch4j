package org.chomookun.arch4j.core.execution.dao;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.chomookun.arch4j.core.execution.model.Execution;
import org.chomookun.arch4j.core.execution.model.ExecutionSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ExecutionRepository extends JpaRepository<ExecutionEntity, String>, JpaSpecificationExecutor<ExecutionEntity> {

    default Page<ExecutionEntity> findAll(ExecutionSearch executionSearch, Pageable pageable) {
        Specification<ExecutionEntity> specification = (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            // taskName
            if (executionSearch.getTaskName() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(
                        root.get(ExecutionEntity_.TASK_NAME), '%' + executionSearch.getTaskName() + '%'
                ));
            }
            // status
            if (executionSearch.getStatus() != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(
                        root.get(ExecutionEntity_.STATUS), executionSearch.getStatus()
                ));
            }

            // conjunction order by
            List<Order> orders = new ArrayList<>();
            pageable.getSort().forEach(sort -> {
                Path<Object> path = root.get(sort.getProperty());
                Order order = sort.isDescending() ? criteriaBuilder.desc(path) : criteriaBuilder.asc(path);
                orders.add(order);
            });
            // status
            Expression<Object> caseWhen = criteriaBuilder.selectCase()
                    .when(criteriaBuilder.equal(root.get(ExecutionEntity_.STATUS), Execution.Status.RUNNING), 1)
                    .otherwise(0);
            orders.add(criteriaBuilder.desc(caseWhen));
            // startedAt
            orders.add(criteriaBuilder.desc(root.get(ExecutionEntity_.STARTED_AT)));
            // adds
            query.orderBy(orders);

            // returns predicate
            return predicate;
        };

        return findAll(specification, pageable);
    }

}
