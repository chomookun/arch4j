package org.chomookun.arch4j.core.example.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;

@Entity
@Table(name = "core_example_backup_item")
@IdClass(ExampleBackupItemEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ExampleBackupItemEntity extends BaseEntity {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk {
        private String exampleId;
        private String itemId;
    }

    @Id
    @Column(name = "example_id", length = 32)
    private String exampleId;

    @Id
    @Column(name = "item_id", length = 32)
    private String itemId;

    @Column(name = "sort")
    private Integer sort;

    @Column(name = "name")
    private String name;

}
