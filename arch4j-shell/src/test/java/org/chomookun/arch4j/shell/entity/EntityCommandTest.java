package org.chomookun.arch4j.shell.entity;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.user.entity.UserEntity;
import org.chomookun.arch4j.shell.common.test.ShellTestSupport;
import org.junit.jupiter.api.Test;

import java.util.List;

@RequiredArgsConstructor
class EntityCommandTest extends ShellTestSupport {

    private final EntityCommand entityCommand;

    @Test
    void list() {
        entityCommand.list();
    }

    @Test
    void generateDdl() {
        // when
        setIn("h2");
        entityCommand.generateDdl(null);
    }

    @Test
    void generateDdlWithSingleEntity() {
        // when
        setIn("h2");
        entityCommand.generateDdl(List.of("UserEntity"));
    }

    @Test
    void generateDdlWithMultipleEntity() {
        // when
        setIn("h2");
        entityCommand.generateDdl(List.of("UserEntity", "RoleEntity"));
    }

    @Test
    void getTableName() {
        // when
        String tableName = entityCommand.getTableName(UserEntity.class);
        // then
        System.out.printf("tableName: %s", tableName);
    }

}