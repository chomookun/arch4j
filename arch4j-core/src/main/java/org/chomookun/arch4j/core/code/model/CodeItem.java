package org.chomookun.arch4j.core.code.model;

import lombok.*;
import org.chomookun.arch4j.core.code.entity.CodeItemEntity;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeItem {

    private String codeId;

    private String itemId;

    private String name;

    private int sort;

    private boolean enabled;

    /**
     * Factory method
     * @param codeItemEntity code item entity
     * @return code item
     */
    public static CodeItem from(CodeItemEntity codeItemEntity) {
        return CodeItem.builder()
                .codeId(codeItemEntity.getCodeId())
                .itemId(codeItemEntity.getItemId())
                .name(codeItemEntity.getName())
                .sort(codeItemEntity.getSort())
                .enabled(codeItemEntity.isEnabled())
                .build();
    }

}
