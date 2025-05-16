package org.chomookun.arch4j.core.code.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.code.entity.CodeEntity;
import org.chomookun.arch4j.core.common.data.BaseModel;
import org.chomookun.arch4j.core.common.i18n.I18nSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Code extends BaseModel implements I18nSupport<CodeI18n> {
	
	private String codeId;

    /**
     * Sets localized name
     * @param name name
     */
    public void setName(String name) {
        i18nSet(i18n -> i18n.setName(name));
    }

    /**
     * Gets localized name
     * @return localized name
     */
    public String getName() {
        return i18nGet(CodeI18n::getName);
    }

    private String note;

    @Builder.Default
	private List<CodeItem> items = new ArrayList<>();

    @Builder.Default
    @JsonIgnore
    private List<CodeI18n> i18ns = new ArrayList<>();

    @Override
    public CodeI18n provideNewI18n(String locale) {
        return CodeI18n.builder()
                .codeId(this.codeId)
                .locale(locale)
                .build();
    }

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
                .note(codeEntity.getNote())
                .build();
        // items
        code.setItems(codeEntity.getItemEntities().stream()
                .map(CodeItem::from)
                .collect(Collectors.toList()));
        // i18ns
        code.setI18ns(codeEntity.getI18ns().stream()
                .map(CodeI18n::from)
                .collect(Collectors.toList()));
        // returns
        return code;
    }

}
