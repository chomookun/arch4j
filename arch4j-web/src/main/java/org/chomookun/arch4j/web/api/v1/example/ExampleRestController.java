package org.chomookun.arch4j.web.api.v1.example;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.example.ExampleService;
import org.chomookun.arch4j.core.example.MybatisExampleService;
import org.chomookun.arch4j.core.example.QueryDslExampleService;
import org.chomookun.arch4j.core.example.model.Example;
import org.chomookun.arch4j.core.example.model.ExampleItem;
import org.chomookun.arch4j.core.example.model.ExampleSearch;
import org.chomookun.arch4j.web.api.v1.example.dto.ExampleRequest;
import org.chomookun.arch4j.web.api.v1.example.dto.ExampleResponse;
import org.chomookun.arch4j.core.common.data.PageableUtils;
import org.chomookun.arch4j.web.common.doc.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "example")
@RestController
@RequestMapping("api/v1/examples")
@PreAuthorize("hasAuthority('example')")
@RequiredArgsConstructor
public class ExampleRestController {

    private final ExampleService exampleService;

    private final MybatisExampleService mybatisExampleService;

    private final QueryDslExampleService queryDslExampleService;

    @Operation(summary = "Returns list of example")
    @Parameter(name = "pageable", hidden = true) @PageableAsQueryParam
    @Parameter(name = "Prefer", schema = @Schema(allowableValues = {"dao-mybatis"}))
    @GetMapping
    public ResponseEntity<List<ExampleResponse>> getExamples(
            @RequestParam(value = "exampleId", required = false) String exampleId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) Example.Type type,
            @RequestParam(value = "code", required = false) String code,
            @PageableDefault Pageable pageable,
            @RequestHeader(value = "Prefer", required = false) String prefer
    ) {
        ExampleSearch exampleSearch = ExampleSearch.builder()
                .exampleId(exampleId)
                .name(name)
                .type(type)
                .code(code)
                .build();
        // switch by prefer header
        Page<Example> examplePage = switch (Optional.ofNullable(prefer).orElse("")) {
            case "dao=mybatis" -> mybatisExampleService.getExamples(exampleSearch, pageable);
            case "dao=queryDsl" -> queryDslExampleService.getExamples(exampleSearch, pageable);
            default -> exampleService.getExamples(exampleSearch, pageable);
        };
        List<ExampleResponse> exampleResponses = examplePage.stream()
                .map(ExampleResponse::from)
                .toList();
        long total = examplePage.getTotalElements();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_RANGE, PageableUtils.toContentRange("example", pageable, total))
                .body(exampleResponses);
    }

    @Operation(summary = "Returns the specified example")
    @Parameter(name = "exampleId", in = ParameterIn.PATH, required = true)
    @Parameter(name = "Prefer", schema = @Schema(allowableValues = {"dao-mybatis"}))
    @GetMapping("{exampleId}")
    public ResponseEntity<ExampleResponse> getExample(@PathVariable("exampleId") String exampleId, @RequestHeader(value = "Prefer", required = false) String prefer) {
        Example example = switch (Optional.ofNullable(prefer).orElse("")) {
            case "dao=mybatis" -> mybatisExampleService.getExample(exampleId).orElseThrow();
            default -> exampleService.getExample(exampleId).orElseThrow();
        };
        ExampleResponse exampleResponse = ExampleResponse.from(example);
        return ResponseEntity.ok(exampleResponse);
    }

    @Operation(summary = "Creates new example")
    @Parameter(name = "Prefer", schema = @Schema(allowableValues = {"dao-mybatis"}))
    @PostMapping
    @Transactional
    @PreAuthorize("hasAuthority('example:edit')")
    public ResponseEntity<ExampleResponse> createExample(@RequestBody @Validated ExampleRequest exampleRequest, @RequestHeader(value = "Prefer", required = false) String prefer) {
        // example
        Example example = Example.builder()
                .exampleId(exampleRequest.getExampleId())
                .name(exampleRequest.getName())
                .icon(exampleRequest.getIcon())
                .number(exampleRequest.getNumber())
                .decimalNumber(exampleRequest.getDecimalNumber())
                .dateTime(exampleRequest.getDateTime())
                .instantDateTime(exampleRequest.getInstantDateTime())
                .zonedDateTime(exampleRequest.getZonedDateTime())
                .date(exampleRequest.getDate())
                .time(exampleRequest.getTime())
                .enabled(exampleRequest.isEnabled())
                .type(exampleRequest.getType())
                .code(exampleRequest.getCode())
                .text(exampleRequest.getText())
                .cryptoText(exampleRequest.getCryptoText())
                .build();
        // example items
        List<ExampleItem> exampleItems = exampleRequest.getExampleItems().stream()
                .map(it -> ExampleItem.builder()
                        .exampleId(example.getExampleId())
                        .itemId(it.getItemId())
                        .name(it.getName())
                        .build())
                .collect(Collectors.toList());
        example.setExampleItems(exampleItems);
        // calls service
        Example savedExample = switch (Optional.ofNullable(prefer).orElse("")) {
            case "dao=mybatis" -> mybatisExampleService.saveExample(example);
            default -> exampleService.saveExample(example);
        };
        ExampleResponse exampleResponse = ExampleResponse.from(savedExample);
        return ResponseEntity.ok(exampleResponse);
    }

    @Operation(summary = "Modifies the specified example")
    @Parameter(name = "exampleId", required = true, in = ParameterIn.PATH)
    @Parameter(name = "Prefer", schema = @Schema(allowableValues = {"dao-mybatis"}))
    @PutMapping("{exampleId}")
    @Transactional
    @PreAuthorize("hasAuthority('example:edit')")
    public ResponseEntity<ExampleResponse> modifyExample(@PathVariable("exampleId") String exampleId, @RequestBody @Validated ExampleRequest exampleRequest, @RequestHeader(value = "Prefer", required = false) String prefer) {
        Example example = exampleService.getExample(exampleId).orElseThrow();
        example.setName(exampleRequest.getName());
        example.setIcon(exampleRequest.getIcon());
        example.setNumber(exampleRequest.getNumber());
        example.setDecimalNumber(exampleRequest.getDecimalNumber());
        example.setDateTime(exampleRequest.getDateTime());
        example.setInstantDateTime(exampleRequest.getInstantDateTime());
        example.setZonedDateTime(exampleRequest.getZonedDateTime());
        example.setDate(exampleRequest.getDate());
        example.setTime(exampleRequest.getTime());
        example.setEnabled(exampleRequest.isEnabled());
        example.setType(exampleRequest.getType());
        example.setCode(exampleRequest.getCode());
        example.setText(exampleRequest.getText());
        example.setCryptoText(exampleRequest.getCryptoText());
        // example items
        List<ExampleItem> exampleItems = exampleRequest.getExampleItems().stream()
                .map(it -> ExampleItem.builder()
                        .exampleId(example.getExampleId())
                        .itemId(it.getItemId())
                        .name(it.getName())
                        .build())
                .collect(Collectors.toList());
        example.setExampleItems(exampleItems);
        // calls service
        Example savedExample = switch (Optional.ofNullable(prefer).orElse("")) {
            case "dao=mybatis" -> mybatisExampleService.saveExample(example);
            default -> exampleService.saveExample(example);
        };
        ExampleResponse exampleResponse = ExampleResponse.from(savedExample);
        return ResponseEntity.ok(exampleResponse);
    }

    @Operation(summary = "Deletes the specified example")
    @Parameter(name = "Prefer", schema = @Schema(allowableValues = {"dao-mybatis"}))
    @DeleteMapping("{exampleId}")
    @Transactional
    @PreAuthorize("hasAuthority('example:edit')")
    public ResponseEntity<Void> deleteExample(@PathVariable("exampleId") String exampleId, @RequestHeader(value = "Prefer", required = false) String prefer) {
        switch (Optional.ofNullable(prefer).orElse("")) {
            case "dao=mybatis" -> mybatisExampleService.deleteExample(exampleId);
            default -> exampleService.deleteExample(exampleId);
        }
        return ResponseEntity.noContent().build();
    }


}
