package org.chomookun.arch4j.core.common.i18n.test2;

import org.chomookun.arch4j.core.common.i18n.I18n;
import org.chomookun.arch4j.core.common.i18n.I18nSupport;
import org.chomookun.arch4j.core.common.i18n.I18nUtils;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class I18nGetter<T extends I18n> {

    private final I18nSupport<T> target;

    private final String currentValue;

    private Supplier<String> defaultLocaleSupplier;

    private Function<T, String> i18nLocaleFunction;

    public I18nGetter(I18nSupport<T> target, String currentValue) {
        this.target = target;
        this.currentValue = currentValue;;
    }

    public I18nGetter<T> ifDefaultLocale(Supplier<String> defaultLocaleSupplier) {
        this.defaultLocaleSupplier = defaultLocaleSupplier;
        return this;
    }

    public I18nGetter<T> ifI18nLocale(Function<T, String> i18nLocaleFunction) {
        this.i18nLocaleFunction = i18nLocaleFunction;
        return this;
    }

    public String get() {
        if (I18nUtils.isDefaultLocale()) {
            return defaultLocaleSupplier.get();
        } else {
            T i18n = target.getI18ns().stream()
                    .filter(it -> Objects.equals(it.getLocale(), I18nUtils.getCurrentLocale()))
                    .findFirst()
                    .orElse(null);
            if (i18n != null) {
                return i18nLocaleFunction.apply(i18n);
            } else {
                return defaultLocaleSupplier.get();
            }
        }
    }

}
