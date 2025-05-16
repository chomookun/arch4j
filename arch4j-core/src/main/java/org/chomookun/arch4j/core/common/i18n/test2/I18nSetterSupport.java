package org.chomookun.arch4j.core.common.i18n.test2;

import org.chomookun.arch4j.core.common.i18n.I18n;
import org.chomookun.arch4j.core.common.i18n.I18nSupport;

public interface I18nSetterSupport<T extends I18n> extends I18nSupport<T> {

    T provideNewI18n(String locale);

    default I18nSetter<T> setter(String currentValue) {
        return new I18nSetter<T>(this, currentValue);
    }

}
