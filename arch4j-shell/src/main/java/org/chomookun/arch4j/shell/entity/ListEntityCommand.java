package org.chomookun.arch4j.shell.entity;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import jakarta.persistence.metamodel.EntityType;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Set;

@ShellComponent
@ShellCommandGroup("Entity Command")
@RequiredArgsConstructor
public class ListEntityCommand extends AbstractEntityCommand {

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

}
