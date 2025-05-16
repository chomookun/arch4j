package org.chomookun.arch4j.core.common.i18n;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public interface I18nSupport<T extends I18n> {

    /**
     * Returns list of I18n objects.
     * @return list of I18n objects
     */
    List<T> getI18ns();

    /**
     * Provides new I18n object.
     * @param locale locale
     * @return new I18n object
     */
    T provideNewI18n(String locale);

    /**
     * Returns specified I18n object.
     * @return I18n object
     */
    default Optional<T> getI18n(String locale) {
        return getI18ns().stream()
                .filter(it -> Objects.equals(it.getLocale(), locale))
                .findFirst();
    }

    /**
     * Sets I18n object for the current locale and default locale.
     * @param consumer consumer function
     */
    default void i18nSet(Consumer<T> consumer) {
        // current locale
        String locale = I18nUtils.getCurrentLocale();
        T i18n = getI18n(locale).orElse(null);
        if (i18n == null) {
            i18n = provideNewI18n(locale);
            getI18ns().add(i18n);
        }
        consumer.accept(i18n);

        // fallback locale
        String defaultLocale = I18nUtils.getDefaultLocale();
        T defaultI18n = getI18n(defaultLocale).orElse(null);
        if (defaultI18n == null) {
            defaultI18n = provideNewI18n(defaultLocale);
            getI18ns().add(defaultI18n);
            consumer.accept(defaultI18n);
        }
    }

    /**
     * Returns localized value of the given getter.
     * @param function function
     * @return localized value
     */
    default String i18nGet(Function<T,String> function) {
        // current locale
        String locale = I18nUtils.getCurrentLocale();
        T i18n = getI18n(locale).orElse(null);
        if (i18n != null) {
            return function.apply(i18n);
        }

        // fallback locale
        String defaultLocale = I18nUtils.getDefaultLocale();
        T defaultI18n = getI18n(defaultLocale).orElse(null);
        if (defaultI18n != null) {
            return function.apply(defaultI18n);
        }

        // final
        return null;
    }

}
