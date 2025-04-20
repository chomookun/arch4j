package org.chomookun.arch4j.web.view.board.widget;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.board.model.Board;
import org.chomookun.arch4j.core.board.BoardService;
import org.chomookun.arch4j.core.page.model.PageWidgetDefinition;
import org.chomookun.arch4j.core.page.model.PageWidgetAware;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Properties;

@Controller
@RequestMapping("/board/{boardId}/widget/latest-article")
@RequiredArgsConstructor
public class LatestArticleController extends PageWidgetAware {

    private final BoardService boardService;

    @Override
    public PageWidgetDefinition getDefinition() {
        return PageWidgetDefinition.builder()
                .name("latest-article")
                .type(this.getClass().getName())
                .propertiesTemplate("boardId=[Board ID]\npageSize=10\n")
                .build();
    }

    @Override
    public String getUrl(Properties properties) {
        String boardId = properties.getProperty("boardId");
        String pageSize = properties.getProperty("pageSize");
        return String.format("/board/%s/widget/latest-article?_size=%s", boardId, pageSize);
    }

    @GetMapping
    public ModelAndView index(@PathVariable("boardId") String boardId, @PageableDefault(size = 10) Pageable pageable) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        ModelAndView modelAndView = new ModelAndView("board/widget/latest-article");
        modelAndView.addObject("board", board);
        modelAndView.addObject("pageable", pageable);
        return modelAndView;
    }

}
