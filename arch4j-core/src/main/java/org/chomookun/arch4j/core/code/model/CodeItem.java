package org.chomookun.arch4j.core.code.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.chomookun.arch4j.core.code.entity.CodeItemEntity;
import org.chomookun.arch4j.core.common.i18n.I18nSupport;

import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeItem implements I18nSupport<CodeItemI18n> {

    private String codeId;

    private String itemId;

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
        return i18nGet(CodeItemI18n::getName);
    }

    private int sort;

    private boolean enabled;

    @Builder.Default
    @JsonIgnore
    private List<CodeItemI18n> i18ns = new ArrayList<>();

    @Override
    public CodeItemI18n provideNewI18n(String locale) {
        return CodeItemI18n.builder()
                .codeId(this.codeId)
                .itemId(this.itemId)
                .locale(locale)
                .build();
    }

    /**
     * Factory method
     * @param codeItemEntity code item entity
     * @return code item
     */
    public static CodeItem from(CodeItemEntity codeItemEntity) {
        CodeItem codeItem = CodeItem.builder()
                .codeId(codeItemEntity.getCodeId())
                .itemId(codeItemEntity.getItemId())
                .sort(codeItemEntity.getSort())
                .enabled(codeItemEntity.isEnabled())
                .build();
        // i18ns
        codeItem.setI18ns(codeItemEntity.getI18ns().stream()
                .map(CodeItemI18n::from)
                .toList());
        // returns
        return codeItem;
    }

}
