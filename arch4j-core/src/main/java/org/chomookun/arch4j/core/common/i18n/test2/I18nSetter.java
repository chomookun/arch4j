package org.chomookun.arch4j.core.common.i18n.test2;

import org.chomookun.arch4j.core.common.i18n.I18n;
import org.chomookun.arch4j.core.common.i18n.I18nSupport;
import org.chomookun.arch4j.core.common.i18n.I18nUtils;

import java.util.function.Consumer;

public class I18nSetter<T extends I18n> {

    private final I18nSupport<T> target;

    private final String currentValue;

    private Runnable defaultLocaleRunnable;

    private Consumer<T> i18nLocaleConsumer;

    public I18nSetter(I18nSupport<T> target, String currentValue) {
        this.target = target;
        this.currentValue = currentValue;
    }

    public I18nSetter<T> ifDefaultLocale(Runnable runnable) {
        this.defaultLocaleRunnable = runnable;
        return this;
    }

    public I18nSetter<T> ifI18nLocale(Consumer<T> consumer) {
        this.i18nLocaleConsumer = consumer;
        return this;
    }

    public void set() {
        if (I18nUtils.isDefaultLocale()) {
            defaultLocaleRunnable.run();
        } else {
            String locale = I18nUtils.getCurrentLocale();
            T i18n = target.getI18n(locale)
                    .orElse(null);
            if (i18n == null) {
                i18n = target.provideNewI18n(locale);
                target.getI18ns().add(i18n);
            }
            i18nLocaleConsumer.accept(i18n);
            // fallback
            if (currentValue == null) {
                defaultLocaleRunnable.run();
            }
        }
    }

}
