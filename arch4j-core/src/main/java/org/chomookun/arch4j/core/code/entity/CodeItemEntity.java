package org.chomookun.arch4j.core.code.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.data.converter.BooleanConverter;
import org.chomookun.arch4j.core.common.i18n.I18nSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "core_code_item")
@IdClass(CodeItemEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeItemEntity extends BaseEntity implements I18nSupport<CodeItemI18nEntity> {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Pk implements Serializable {
		private String codeId;
		private String itemId;
	}

	@Id
	@Column(name = "code_id", length = 32)
	private String codeId;
	
	@Id
	@Column(name = "item_id", length = 32)
	private String itemId;

    /**
     * Sets item name
     * @param name name
     */
    public void setName(String name) {
        i18nSet(i18n -> i18n.setName(name));
    }

    /**
     * Gets localized item name
     * @return item name
     */
    public String getName() {
        return i18nGet(CodeItemI18nEntity::getName);
    }

    @Column(name = "sort")
	private Integer sort;

    @Column(name = "enabled")
    @Convert(converter = BooleanConverter.class)
    private boolean enabled;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "code_id", insertable = false, updatable = false),
            @JoinColumn(name = "item_id", insertable = false, updatable = false)
    })
    @Builder.Default
    private List<CodeItemI18nEntity> i18ns = new ArrayList<>();

    @Override
    public CodeItemI18nEntity provideNewI18n(String locale) {
        return CodeItemI18nEntity.builder()
                .codeId(this.codeId)
                .itemId(this.itemId)
                .locale(locale)
                .build();
    }

}
