package org.chomookun.arch4j.core.variable.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;

import jakarta.persistence.*;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "core_variable")
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class VariableEntity extends BaseEntity {

    @Id
    @Column(name = "variable_id", length = 128)
    @Comment("Variable ID")
    private String variableId;

    @Column(name = "name")
    @Comment("Name")
    private String name;

    @Column(name = "value", length = Integer.MAX_VALUE)
    @Lob
    @Comment("Value")
    private String value;

    @Column(name = "note", length = Integer.MAX_VALUE)
    @Lob
    @Comment("Note")
    private String note;

}
