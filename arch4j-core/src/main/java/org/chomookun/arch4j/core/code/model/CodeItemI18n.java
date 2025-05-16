package org.chomookun.arch4j.core.code.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.code.entity.CodeItemI18nEntity;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.i18n.I18n;

@SuperBuilder
@Getter
public class CodeItemI18n extends BaseEntity implements I18n {

    private String codeId;

    private String itemId;

    private String locale;

    @Setter
    private String name;

    public static CodeItemI18n from(CodeItemI18nEntity codeItemI18nEntity) {
        return CodeItemI18n.builder()
                .codeId(codeItemI18nEntity.getCodeId())
                .itemId(codeItemI18nEntity.getItemId())
                .locale(codeItemI18nEntity.getLocale())
                .name(codeItemI18nEntity.getName())
                .build();
    }

}

