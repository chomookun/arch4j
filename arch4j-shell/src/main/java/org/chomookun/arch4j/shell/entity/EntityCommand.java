package org.chomookun.arch4j.shell.entity;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
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
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.*;
import java.util.stream.Stream;

@ShellComponent
@RequiredArgsConstructor
public class EntityCommand {

    private final EntityManager entityManager;

    private final EntityManagerFactory entityManagerFactory;

    @ShellMethod(key = "entity list", value = "Entity list command")
    public void list() {
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        AsciiTable table = new AsciiTable();
        table.getRenderer().setCWC(new CWC_LongestLine());
        table.addRule();
        table.addRow("Entity Name", "Table Name", "Class Name");
        table.addRule();
        for (EntityType<?> entity : entities) {
            String entityName = entity.getName();
            String tableName = getTableName(entity.getJavaType());
            String className = entity.getJavaType().getName();
            table.addRow(entityName, tableName, className);
            table.addRule();
        }
        System.out.println(table.render());
    }

    @ShellMethod(key = "entity generate-ddl", value = "Generate DDL command")
    public void generateDdl(@ShellOption(defaultValue = ShellOption.NULL, arity = Integer.MAX_VALUE) List<String> entityNames) {
        // selects dialect
        Map<String,String> dialectOptions = new LinkedHashMap<String,String>() {{
            put("h2", org.hibernate.dialect.H2Dialect.class.getName());
            put("mariadb", org.hibernate.dialect.MariaDBDialect.class.getName());
            put("mysql", org.hibernate.dialect.MySQLDialect.class.getName());
            put("postgresql", org.hibernate.dialect.PostgreSQLDialect.class.getName());
            put("oracle", org.hibernate.dialect.OracleDialect.class.getName());
            put("sqlserver", org.hibernate.dialect.SQLServerDialect.class.getName());
            put("db2", org.hibernate.dialect.DB2Dialect.class.getName());
        }};
        String dialectAnswer = InteractiveUtil.askSelect("Select JPA Dialect", dialectOptions);
        String dialectClassName = dialectOptions.get(dialectAnswer);
        // creates metadata
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.dialect", dialectClassName)
                .applySetting("hibernate.connection.datasource", "")
                .applySetting("hibernate.connection.provider_class", org.hibernate.engine.jdbc.connections.spi.ConnectionProvider.class.getName())
                .applySetting("hibernate.temp.use_jdbc_metadata_defaults", "false")
                .applySetting("hibernate.hbm2ddl.auto", "none")
                .build();
        MetadataSources metadataSources = new MetadataSources(registry);
        Metamodel metamodel = entityManagerFactory.getMetamodel();
        for (EntityType<?> entityType : metamodel.getEntities()) {
            metadataSources.addAnnotatedClass(entityType.getJavaType());
        }
        Metadata metadata = metadataSources.buildMetadata();
        // finds specified table
        Stream<EntityType<?>> stream = entityManager.getMetamodel().getEntities().stream();
        if (entityNames != null && !entityNames.isEmpty()) {
            stream = stream.filter(entity -> entityNames.contains(entity.getName()));
        }
        List<Table> tables = stream.map(entityType -> {
            Class<?> entityClass = entityType.getJavaType();
            PersistentClass persistentClass = metadata.getEntityBinding(entityClass.getName());
            return persistentClass.getTable();
        }).toList();
        // creates table creation ddl
        JdbcEnvironment jdbcEnvironment = Optional.ofNullable(registry.getService(JdbcEnvironment.class)).orElseThrow();
        SqlStringGenerationContext sqlStringGenerationContext = SqlStringGenerationContextImpl.fromConfigurationMap(
                jdbcEnvironment, metadata.getDatabase(), new HashMap<>()
        );
        Dialect dialect = metadata.getDatabase().getDialect();
        StandardTableExporter tableExporter = new StandardTableExporter(dialect);
        StandardIndexExporter indexExporter = new StandardIndexExporter(dialect);
        for (Table table : tables) {
            // print table sql
            String[] tableSql = tableExporter.getSqlCreateStrings(table, metadata, sqlStringGenerationContext);
            String formattedSql = FormatStyle.DDL.getFormatter().format(tableSql[0]);
            System.out.println('\n' + formattedSql + ";");
            // print index sql
            table.getIndexes().values().forEach(index -> {
                String[] indexSql = indexExporter.getSqlCreateStrings(index, metadata, sqlStringGenerationContext);
                Arrays.stream(indexSql).forEach(sql -> {
                    System.out.println('\n' + sql + ";");
                });
            });
        }
    }

    String getTableName(Class<?> entityClass) {
        jakarta.persistence.Table table = entityClass.getAnnotation(jakarta.persistence.Table.class);
        return (table != null && !table.name().isEmpty()) ? table.name() : entityClass.getSimpleName().toLowerCase();
    }

}
