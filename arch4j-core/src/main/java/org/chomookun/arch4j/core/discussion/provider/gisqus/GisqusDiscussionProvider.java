package org.chomookun.arch4j.core.discussion.provider.gisqus;

import org.chomookun.arch4j.core.discussion.provider.DiscussionProvider;

import java.util.Properties;

public class GisqusDiscussionProvider extends DiscussionProvider {

    public GisqusDiscussionProvider(Properties config) {
        super(config);
    }

    @Override
    public String getViewName() {
        return "discussion/provider/gisqus";
    }

}
