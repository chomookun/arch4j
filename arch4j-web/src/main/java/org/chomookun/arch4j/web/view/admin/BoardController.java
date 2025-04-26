package org.chomookun.arch4j.web.view.admin;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.board.model.Board;
import org.chomookun.arch4j.core.board.model.BoardSearch;
import org.chomookun.arch4j.core.board.BoardService;
import org.chomookun.arch4j.core.discussion.DiscussionService;
import org.chomookun.arch4j.core.discussion.model.Discussion;
import org.chomookun.arch4j.core.discussion.model.DiscussionSearch;
import org.chomookun.arch4j.web.WebProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@ConditionalOnProperty(prefix = "web.admin", name = "enabled", havingValue = "true", matchIfMissing = false)
@RequestMapping("admin/board")
@PreAuthorize("hasAuthority('admin.board')")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final DiscussionService discussionService;

    private final WebProperties webProperties;

    @GetMapping
    public ModelAndView index() throws IOException {
        ModelAndView modelAndView = new ModelAndView("admin/board");
        modelAndView.addObject("skinNames", getSkinNames());
        // discussions
        List<Discussion> discussions = discussionService.getDiscussions(DiscussionSearch.builder().build(), Pageable.unpaged()).getContent();
        modelAndView.addObject("discussions", discussions);
        return modelAndView;
    }

    protected Set<String> getSkinNames() throws IOException {
        Set<String> skinNames = new HashSet<>();
        String resourcePattern = String.format("classpath*:templates/_theme/%s/board/*", webProperties.getTheme());
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(resourcePattern);
        for (Resource resource : resources) {
            String resourcePath = resource.getURL().getPath();
            String[] pathParts = resourcePath.split("/");
            String lastDirectoryName = pathParts[pathParts.length - 1];
            skinNames.add(lastDirectoryName);
        }
        return skinNames;
    }

    @GetMapping("get-boards")
    @ResponseBody
    public Page<Board> getBoard(BoardSearch boardSearch, Pageable pageable) {
        return boardService.getBoards(boardSearch, pageable);
    }

    @GetMapping("get-board")
    @ResponseBody
    public Board getBoard(@RequestParam("boardId")String boardId) {
        return boardService.getBoard(boardId).orElseThrow();
    }

    @PostMapping("save-board")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.board:edit')")
    @Transactional
    public Board saveVariable(@RequestBody @Valid Board board) {
        return boardService.saveBoard(board);
    }

    @GetMapping("delete-board")
    @ResponseBody
    @PreAuthorize("hasAuthority('admin.board:edit')")
    @Transactional
    public void deleteBoard(@RequestParam("boardId")String boardId) {
        boardService.deleteBoard(boardId);
    }

}
