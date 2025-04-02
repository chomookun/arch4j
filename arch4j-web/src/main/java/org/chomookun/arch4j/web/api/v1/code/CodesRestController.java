package org.chomookun.arch4j.web.api.v1.code;

import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/codes")
@RequiredArgsConstructor
@Tag(name = "code", description = "Codes")
public class CodesRestController {

    private final CodeService codeService;

    /**
     * Gets codes
     * @param codeId code id
     * @param name name
     * @param pageable pageable
     * @return code responses
     */
    @GetMapping
    @Operation(summary = "Gets list of CodeEntity", description = "returns code list")
    @PageableAsQueryParam
    public ResponseEntity<List<CodeResponse>> getCodes(
            @RequestParam(value = "codeId", required = false) String codeId,
            @RequestParam(value = "name", required = false) String name,
            Pageable pageable
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

    /**
     * Gets code
     * @param codeId code id
     * @return code response
     */
    @GetMapping("{codeId}")
    public ResponseEntity<CodeResponse> getCode(@PathVariable("codeId") String codeId) {
        Code code = codeService.getCode(codeId).orElseThrow();
        CodeResponse codeResponse = CodeResponse.from(code);
        return ResponseEntity.ok(codeResponse);
    }

}
