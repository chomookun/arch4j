package org.chomookun.arch4j.core.common.i18n.test1;

import org.chomookun.arch4j.core.CorePropertiesHolder;
import org.springframework.context.i18n.LocaleContextHolder;

public class I18nUtils {

    /**
     * Checks if current locale is default locale
     * @return true if current locale is default locale
     */
    public static boolean isDefaultLocale() {
        return LocaleContextHolder.getLocale()
                .equals(CorePropertiesHolder.getInstance().getDefaultLocale());
    }

    /**
     * Gets current locale
     * @return current locale (language code)
     */
    public static String getCurrentLocale() {
        return LocaleContextHolder.getLocale().getLanguage();
    }

}
