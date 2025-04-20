package org.chomookun.arch4j.core.storage.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StorageObjectSearch {

    private String group;

    private String filename;

    private String storageId;

}
