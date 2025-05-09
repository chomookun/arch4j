package org.chomookun.arch4j.shell.entity;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.shell.common.InteractiveUtil;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.relational.SqlStringGenerationContext;
import org.hibernate.boot.model.relational.internal.SqlStringGenerationContextImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;
import org.hibernate.tool.schema.internal.StandardIndexExporter;
import org.hibernate.tool.schema.internal.StandardTableExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.*;
import java.util.stream.Stream;

@Getter
public class AbstractEntityCommand {

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected EntityManagerFactory entityManagerFactory;

    String getTableName(Class<?> entityClass) {
        jakarta.persistence.Table table = entityClass.getAnnotation(jakarta.persistence.Table.class);
        return (table != null && !table.name().isEmpty()) ? table.name() : entityClass.getSimpleName().toLowerCase();
    }

}
