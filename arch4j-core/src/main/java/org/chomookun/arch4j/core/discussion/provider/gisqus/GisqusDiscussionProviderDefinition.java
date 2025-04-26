package org.chomookun.arch4j.core.discussion.provider.gisqus;

import org.chomookun.arch4j.core.discussion.provider.DiscussionProvider;
import org.chomookun.arch4j.core.discussion.provider.DiscussionProviderDefinition;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class GisqusDiscussionProviderDefinition extends DiscussionProviderDefinition {

    public String getDiscussionProviderId() {
        return "gisqus";
    }

    public String getName() {
        return "Gisqus";
    }

    String getConfigTemplate() {
        return """
        repo=[user/repo]
        repo-id=[repo id]
        category=[discussion category]
        category-id=[discussion category id]
        theme-light=light
        theme-dart=dark
        """;
    }

    public Class<? extends DiscussionProvider> getClassType() {
        return GisqusDiscussionProvider.class;
    }

}
