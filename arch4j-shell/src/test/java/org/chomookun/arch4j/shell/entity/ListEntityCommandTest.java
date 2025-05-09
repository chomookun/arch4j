package org.chomookun.arch4j.shell.entity;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.user.entity.UserEntity;
import org.chomookun.arch4j.shell.common.test.ShellTestSupport;
import org.junit.jupiter.api.Test;

import java.util.List;

@RequiredArgsConstructor
class ListEntityCommandTest extends ShellTestSupport {

    private final ListEntityCommand listEntityCommand;

    @Test
    void list() {
        listEntityCommand.list();
    }

}