package org.chomookun.arch4j.core.example.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.example.entity.ExampleEntity;
import org.chomookun.arch4j.core.example.entity.ExampleEntity_;
import org.chomookun.arch4j.core.example.entity.QExampleEntity;
import org.chomookun.arch4j.core.example.model.ExampleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ExampleRepositoryCustomImpl implements ExampleRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ExampleEntity> findAllWithQueryDsl(ExampleSearch exampleSearch, Pageable pageable) {
        QExampleEntity qExampleEntity = QExampleEntity.exampleEntity;
        JPAQuery<ExampleEntity> query = jpaQueryFactory
                .selectFrom(qExampleEntity)
                .where(
                        Optional.ofNullable(exampleSearch.getExampleId())
                                .map(qExampleEntity.exampleId::contains)
                                .orElse(null),
                        Optional.ofNullable(exampleSearch.getName())
                                .map(qExampleEntity.name::contains)
                                .orElse(null),
                        Optional.ofNullable(exampleSearch.getType())
                                .map(qExampleEntity.type::eq)
                                .orElse(null),
                        Optional.ofNullable(exampleSearch.getCode())
                                .map(qExampleEntity.code::eq)
                                .orElse(null)
                );
        // sort
        List<OrderSpecifier<?>> orderSpecifiers = pageable.getSort().stream()
                .map(sort -> {
                    ComparableExpressionBase<?> expression = switch (sort.getProperty()) {
                        case ExampleEntity_.NAME -> qExampleEntity.name;
                        case ExampleEntity_.TYPE -> qExampleEntity.type;
                        case ExampleEntity_.CODE -> qExampleEntity.code;
                        default -> throw new IllegalStateException("Unexpected value: " + sort.getProperty());
                    };
                    Order direction = sort.isAscending() ? Order.ASC : Order.DESC;
                    return new OrderSpecifier<>(direction, expression);
                }).collect(Collectors.toList());
        orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, qExampleEntity.exampleId));
        // order by
        query.orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]));
        // list
        JPAQuery<ExampleEntity> listQuery = query.clone();
        if (pageable.isPaged()) {
            listQuery.limit(pageable.getPageSize())
                    .offset(pageable.getOffset());
        }
        List<ExampleEntity> exampleEntities = listQuery.fetch();
        // total
        JPAQuery<ExampleEntity> totalQuery = query.clone();
        totalQuery.getMetadata().clearOrderBy();
        Long total = totalQuery
                .select(qExampleEntity.count())
                .fetchOne();
        total = Optional.ofNullable(total).orElse(0L);
        // return
        return new PageImpl<>(exampleEntities, pageable, total);
    }

}
