package org.chomookun.arch4j.core.common.i18n.test1;

import java.util.List;

public interface I18nSupport<T extends I18n> {

    /**
     * Returns list of I18n objects.
     * @return list of I18n objects
     */
    List<T> getI18ns();

}
