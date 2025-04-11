package org.chomookun.arch4j.core.storage.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StorageSearch {

    private String storageId;

    private String name;

}
