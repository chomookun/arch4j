package org.chomookun.arch4j.shell.entity;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.shell.common.test.ShellTestSupport;
import org.junit.jupiter.api.Test;

import java.util.List;

@RequiredArgsConstructor
class DdlCommandTest extends ShellTestSupport {

    private final DdlCommand ddlCommand;

    @Test
    void generateDdl() {
        // when
        setIn("h2");
        ddlCommand.generateDdl(null);
    }

    @Test
    void generateDdlWithSingleEntity() {
        // when
        setIn("h2");
        ddlCommand.generateDdl(List.of("UserEntity"));
    }

    @Test
    void generateDdlWithMultipleEntity() {
        // when
        setIn("h2");
        ddlCommand.generateDdl(List.of("UserEntity", "RoleEntity"));
    }

}