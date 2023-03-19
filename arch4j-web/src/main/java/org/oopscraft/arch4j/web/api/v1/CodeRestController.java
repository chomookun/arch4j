package org.oopscraft.arch4j.web.api.v1;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.code.Code;
import org.oopscraft.arch4j.core.code.CodeSearch;
import org.oopscraft.arch4j.core.code.CodeService;
import org.oopscraft.arch4j.web.support.PaginationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/code")
@RequiredArgsConstructor
public class CodeRestController {

    private final CodeService codeService;

    @GetMapping
    public ResponseEntity<List<CodeResponse>> getCodes(CodeRequest codeRequest, Pageable pageable) {
        CodeSearch codeSearch = CodeSearch.builder()
                .id(codeRequest.getId())
                .name(codeRequest.getName())
                .build();
        Page<Code> codePage = codeService.getCodes(codeSearch, pageable);
        List<CodeResponse> codeResponses = codePage.stream()
                .map(CodeResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_RANGE, PaginationUtils.toContentRange("code", pageable, codePage))
                .body(codeResponses);
    }

}
