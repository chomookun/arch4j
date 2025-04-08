package org.chomookun.arch4j.web.api.v1.code;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.code.model.Code;
import org.chomookun.arch4j.core.code.model.CodeSearch;
import org.chomookun.arch4j.core.code.CodeService;
import org.chomookun.arch4j.core.common.data.PageableUtils;
import org.chomookun.arch4j.web.api.v1.code.dto.CodeResponse;
import org.chomookun.arch4j.web.common.doc.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "code")
@RestController
@RequestMapping("api/v1/codes")
@RequiredArgsConstructor
public class CodeRestController {

    private final CodeService codeService;

    @Operation(summary = "Returns list of code")
    @Parameter(name = "pageable", hidden = true) @PageableAsQueryParam
    @GetMapping
    public ResponseEntity<List<CodeResponse>> getCodes(
            @RequestParam(value = "codeId", required = false) String codeId,
            @RequestParam(value = "name", required = false) String name,
            @PageableDefault Pageable pageable
    ) {
        CodeSearch codeSearch = CodeSearch.builder()
                .codeId(codeId)
                .name(name)
                .build();
        Page<Code> codePage = codeService.getCodes(codeSearch, pageable);
        List<CodeResponse> codeResponses = codePage.stream()
                .map(CodeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_RANGE, PageableUtils.toContentRange("code", pageable, codePage.getTotalElements()))
                .body(codeResponses);
    }

    @Operation(summary = "Returns the specified code")
    @Parameter(name = "codeId", in =ParameterIn.PATH, example = "core.example.Example.code")
    @GetMapping("{codeId}")
    public ResponseEntity<CodeResponse> getCode(@PathVariable("codeId") String codeId) {
        Code code = codeService.getCode(codeId).orElseThrow();
        CodeResponse codeResponse = CodeResponse.from(code);
        return ResponseEntity.ok(codeResponse);
    }

}
