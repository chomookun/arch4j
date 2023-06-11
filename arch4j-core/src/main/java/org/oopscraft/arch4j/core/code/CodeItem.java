package org.oopscraft.arch4j.core.code;

import lombok.*;
import org.oopscraft.arch4j.core.code.repository.CodeItemEntity;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeItem {

    private String codeId;

    private String itemId;

    private int sort;

    private String name;

    private String value;

    static CodeItem from(CodeItemEntity codeItemEntity) {
        return CodeItem.builder()
                .codeId(codeItemEntity.getCodeId())
                .itemId(codeItemEntity.getItemId())
                .sort(codeItemEntity.getSort())
                .name(codeItemEntity.getName())
                .value(codeItemEntity.getValue())
                .build();
    }

}
