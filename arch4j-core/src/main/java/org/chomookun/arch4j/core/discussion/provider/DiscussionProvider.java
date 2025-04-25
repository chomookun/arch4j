package org.chomookun.arch4j.core.discussion.provider;

import lombok.Getter;

import java.util.Properties;

public class DiscussionProvider {

    @Getter
    private Properties config;

    public DiscussionProvider(Properties config) {
        this.config = config;
    }

    public String getViewName() {
        return "discussion/discussion";
    }

}
