package org.chomookun.arch4j.web.api.v1.page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.page.model.Page;
import org.chomookun.arch4j.core.page.PageService;
import org.chomookun.arch4j.web.api.v1.page.dto.PageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "page")
@RestController
@RequestMapping("api/v1/pages")
@RequiredArgsConstructor
public class PageRestController {

    private final PageService pageService;

    @Operation(summary = "Returns the specified page")
    @Parameter(name = "pageId", in = ParameterIn.PATH, required = true)
    @GetMapping("{pageId}")
    public ResponseEntity<PageResponse> getPage(@PathVariable("pageId")String pageId) {
        Page page = pageService.getPage(pageId).orElseThrow();
        PageResponse pageResponse = PageResponse.from(page);
        return ResponseEntity.ok().body(pageResponse);
    }

}
