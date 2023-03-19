package org.oopscraft.arch4j.web.api.v1;

import lombok.Builder;
import lombok.Data;
import org.oopscraft.arch4j.core.code.Code;
import org.oopscraft.arch4j.core.code.CodeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CodeResponse {

    private String id;

    private String name;

    @Builder.Default
    private List<Item> items = new ArrayList<>();

    /**
     * Item
     */
    @Data
    @Builder
    protected static class Item {

        private String id;

        private String name;

        private String value;

        /**
         * static factory method
         * @param codeItem
         * @return
         */
        static Item from(CodeItem codeItem) {
            return Item.builder()
                    .id(codeItem.getId())
                    .name(codeItem.getName())
                    .value(codeItem.getValue())
                    .build();
        }

    }

    /**
     * static factory method
     * @param code
     * @return
     */
    static CodeResponse from(Code code){
         return CodeResponse.builder()
                .id(code.getId())
                .name(code.getName())
                .items(code.getItems().stream().map(Item::from).collect(Collectors.toList()))
                .build();
    }

}

