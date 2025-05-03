package org.chomookun.arch4j.core.code.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.code.entity.CodeEntity;
import org.chomookun.arch4j.core.common.data.BaseModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Code extends BaseModel {
	
	private String codeId;
	
	private String name;
	
	private String note;

    @Builder.Default
	private List<CodeItem> items = new ArrayList<>();

    /**
     * Factory method
     * @param codeEntity code entity
     * @return code
     */
    public static Code from(CodeEntity codeEntity) {
        Code code = Code.builder()
                .systemRequired(codeEntity.isSystemRequired())
                .systemUpdatedAt(codeEntity.getSystemUpdatedAt())
                .systemUpdatedBy(codeEntity.getSystemUpdatedBy())
                .codeId(codeEntity.getCodeId())
                .name(codeEntity.getName())
                .note(codeEntity.getNote())
                .build();
        // items
        code.setItems(codeEntity.getItemEntities().stream()
                .map(CodeItem::from)
                .collect(Collectors.toList()));
        // returns
        return code;
    }

}
