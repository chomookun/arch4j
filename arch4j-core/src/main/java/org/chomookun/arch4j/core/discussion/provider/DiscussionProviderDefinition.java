package org.chomookun.arch4j.core.discussion.provider;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class DiscussionProviderDefinition {

    public String getDiscussionProviderId() {
        return "default";
    }

    public String getName() {
        return "Default";
    }

    String getConfigTemplate() {
        return "";
    }

    public Class<? extends DiscussionProvider> getClassType() {
        return DiscussionProvider.class;
    }

}
