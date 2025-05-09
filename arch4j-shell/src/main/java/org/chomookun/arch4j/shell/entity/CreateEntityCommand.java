package org.chomookun.arch4j.shell.entity;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.metamodel.EntityType;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.Set;

@ShellComponent
@ShellCommandGroup("Entity Command")
@RequiredArgsConstructor
public class CreateEntityCommand extends AbstractEntityCommand {

    @ShellMethod(key = "entity create", value = "Entity list command")
    public void createEntity(@ShellOption(defaultValue = ShellOption.NULL, arity = Integer.MAX_VALUE) List<String> entityNames) {
        System.out.println("test");
    }

}
