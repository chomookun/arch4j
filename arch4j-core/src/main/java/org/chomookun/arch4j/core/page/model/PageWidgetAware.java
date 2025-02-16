package org.chomookun.arch4j.core.page.model;

import java.util.Properties;

public abstract class PageWidgetAware {

    public abstract PageWidgetDefinition getDefinition();

    public abstract String getUrl(Properties properties) ;

}
