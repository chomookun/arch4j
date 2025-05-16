package org.chomookun.arch4j.core.common.i18n;

import org.chomookun.arch4j.core.CorePropertiesHolder;
import org.springframework.context.i18n.LocaleContextHolder;

public class I18nUtils {

    /**
     * Gets default locale
     * @return default locale (language code)
     */
    public static String getDefaultLocale() {
        return CorePropertiesHolder.getInstance()
                .getDefaultLocale()
                .getLanguage();
    }

    /**
     * Gets current locale
     * @return current locale (language code)
     */
    public static String getCurrentLocale() {
        return LocaleContextHolder.getLocale().getLanguage();
    }

    /**
     * Checks if current locale is default locale
     * @return true if current locale is default locale
     */
    public static boolean isDefaultLocale() {
        return LocaleContextHolder.getLocale()
                .getLanguage().equals(getDefaultLocale());
    }

}
