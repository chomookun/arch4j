package org.chomookun.arch4j.core.discussion.provider;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class DiscussionProviderDefinition {

    public String getProviderType() {
        return "default";
    }

    public String getName() {
        return "Default";
    }

    public Class<? extends DiscussionProvider> getClassType() {
        return DiscussionProvider.class;
    }

    public String getPropertiesTemplate() {
        return "";
    }


}
