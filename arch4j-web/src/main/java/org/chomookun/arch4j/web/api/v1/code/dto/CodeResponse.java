package org.chomookun.arch4j.web.api.v1.code.dto;

import lombok.Builder;
import lombok.Data;
import org.chomookun.arch4j.core.code.model.Code;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CodeResponse {

    private String codeId;

    private String codeName;

    @Builder.Default
    private List<CodeItemResponse> codeItems = new ArrayList<>();

    /**
     * Convert code to code response
     * @param code code
     * @return code response
     */
    public static CodeResponse from(Code code){
        CodeResponse codeResponse = CodeResponse.builder()
                .codeId(code.getCodeId())
                .codeName(code.getName())
                .build();
        List<CodeItemResponse> codeItems = code.getCodeItems().stream()
                .map(CodeItemResponse::from)
                .collect(Collectors.toList());
        codeResponse.setCodeItems(codeItems);
        return codeResponse;
    }

}

