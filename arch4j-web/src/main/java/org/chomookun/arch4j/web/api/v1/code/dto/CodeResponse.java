package org.chomookun.arch4j.web.api.v1.code.dto;

import lombok.Builder;
import lombok.Data;
import org.chomookun.arch4j.core.code.model.Code;
import org.chomookun.arch4j.core.code.model.CodeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CodeResponse {

    private String codeId;

    private String name;

    @Builder.Default
    private List<CodeItemResponse> items = new ArrayList<>();

    /**
     * Factory method
     * @param code code
     * @return code response
     */
    public static CodeResponse from(Code code){
        CodeResponse codeResponse = CodeResponse.builder()
                .codeId(code.getCodeId())
                .name(code.getName())
                .build();
        List<CodeItemResponse> codeItems = code.getItems().stream()
                .filter(CodeItem::isEnabled)        // only enabled
                .map(CodeItemResponse::from)
                .collect(Collectors.toList());
        codeResponse.setItems(codeItems);
        return codeResponse;
    }

}

