package org.chomookun.arch4j.core.common.i18n.test1;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public interface I18nEntitySupport<T extends I18n> extends I18nSupport<T> {

    /**
     * Get or creates I18n object.
     * @param locale locale
     * @param creator creator supplier function
     * @return i18n object
     */
    default T getOrCreateI18n(String locale, Supplier<T> creator) {
        return getI18ns().stream()
                .filter(it -> Objects.equals(it.getLocale(), locale))
                .findFirst()
                .orElseGet(() -> {
                    T created = creator.get();
                    this.getI18ns().add(created);
                    return created;
                });
    }

    List<T> getI18ns();

}
