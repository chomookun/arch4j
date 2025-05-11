package org.chomookun.arch4j.web.view.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.web.common.test.WebTestSupport;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.IOException;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@WithMockUser(username = "admin", authorities = {"admin.board"})
class BoardControllerTest extends WebTestSupport {

    private final BoardController boardController;

    @Test
    public void getSkinNames() throws IOException {
        Set<String> skinNames = boardController.getSkinNames();
        skinNames.forEach(skinName -> log.info("{}", skinName));
    }

}
