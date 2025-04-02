package org.chomookun.arch4j.web.api.v1.code.dto;

import lombok.*;
import org.chomookun.arch4j.core.code.model.CodeItem;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeItemResponse {

    private String itemId;

    private String name;

    private String value;

    /**
     * Convert from code item to code Item response
     * @param codeItem code item
     * @return code item response
     */
    static CodeItemResponse from(CodeItem codeItem) {
        return CodeItemResponse.builder()
                .itemId(codeItem.getItemId())
                .name(codeItem.getName())
                .build();
    }

}
