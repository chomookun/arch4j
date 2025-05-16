package org.chomookun.arch4j.core.code.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.code.entity.CodeI18nEntity;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.i18n.I18n;

@SuperBuilder
@Getter
public class CodeI18n extends BaseEntity implements I18n {

    private String codeId;

    private String locale;

    @Setter
    private String name;

    public static CodeI18n from(CodeI18nEntity entity) {
        return CodeI18n.builder()
                .codeId(entity.getCodeId())
                .locale(entity.getLocale())
                .name(entity.getName())
                .build();
    }

}

