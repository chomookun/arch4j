package org.chomookun.arch4j.core.menu.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.i18n.I18n;
import org.chomookun.arch4j.core.common.data.BaseEntity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_menu_i18n")
@IdClass(MenuI18nEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuI18nEntity extends BaseEntity implements I18n {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String menuId;
        private String locale;
    }

    @Id
    @Column(name = "menu_id", length = 32)
    private String menuId;

    @Id
    @Column(name = "locale", length = 8)
    private String locale;

    @Column(name = "name")
    private String name;

}

