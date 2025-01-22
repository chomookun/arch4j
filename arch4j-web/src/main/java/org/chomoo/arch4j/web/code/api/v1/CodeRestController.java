package org.chomoo.arch4j.web.code.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.chomoo.arch4j.core.code.model.Code;
import org.chomoo.arch4j.core.code.model.CodeSearch;
import org.chomoo.arch4j.core.code.service.CodeService;
import org.chomoo.arch4j.web.code.api.v1.dto.CodeResponse;
import org.chomoo.arch4j.web.common.data.PageableAsQueryParam;
import org.chomoo.arch4j.web.common.data.PageableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/code")
@RequiredArgsConstructor
@Tag(name = "board", description = "Board")
public class CodeRestController {

    private final CodeService codeService;

    @GetMapping
    @Operation(summary = "Gets list of CodeEntity", description = "returns code list")
    @PageableAsQueryParam
    public ResponseEntity<List<CodeResponse>> getCodes(
            @RequestParam(value = "codeId", required = false) String codeId,
            @RequestParam(value = "codeName", required = false) String codeName,
            Pageable pageable
    ) {
        CodeSearch codeSearch = CodeSearch.builder()
                .codeId(codeId)
                .name(codeName)
                .build();

        Page<Code> codePage = codeService.getCodes(codeSearch, pageable);

        List<CodeResponse> codeResponses = codePage.stream()
                .map(CodeResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_RANGE, PageableUtils.toContentRange("code", pageable, codePage.getTotalElements()))
                .body(codeResponses);
    }

}
