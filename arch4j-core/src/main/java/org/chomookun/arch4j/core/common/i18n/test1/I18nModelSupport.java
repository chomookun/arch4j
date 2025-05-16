package org.chomookun.arch4j.core.common.i18n.test1;

import java.util.Objects;
import java.util.function.Function;

public interface I18nModelSupport<T extends I18n> extends I18nSupport<T> {

    /**
     * Returns localized value of the given getter.
     * @param getter getter function
     * @param fallback fallback value
     * @return localized value
     */
    default String localizeValue(Function<T, String> getter, String fallback) {
        String currentLocale = I18nUtils.getCurrentLocale();
        return getI18ns().stream()
                .filter(it -> Objects.equals(it.getLocale(), currentLocale))
                .map(getter)
                .findFirst()
                .orElse(fallback);
    }

}
