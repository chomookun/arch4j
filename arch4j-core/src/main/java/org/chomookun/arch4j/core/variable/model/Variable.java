package org.chomookun.arch4j.core.variable.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseModel;
import org.chomookun.arch4j.core.variable.entity.VariableEntity;

@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Variable extends BaseModel {

    @NotNull
    private String variableId;

    @NotBlank
    private String name;

    private String value;

    private String note;

    /**
     * factory method
     * @param variableEntity variable entity
     * @return variable
     */
    public static Variable from(VariableEntity variableEntity) {
        return Variable.builder()
                .systemRequired(variableEntity.isSystemRequired())
                .systemUpdatedAt(variableEntity.getSystemUpdatedAt())
                .systemUpdatedBy(variableEntity.getSystemUpdatedBy())
                .variableId(variableEntity.getVariableId())
                .value(variableEntity.getValue())
                .name(variableEntity.getName())
                .note(variableEntity.getNote())
                .build();
    }

}
