package org.chomookun.arch4j.core.storage.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StorageFileSearch {

    private String targetType;

    private String targetId;

    private String filename;

}
