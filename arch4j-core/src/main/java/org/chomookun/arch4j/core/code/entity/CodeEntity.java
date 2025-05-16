package org.chomookun.arch4j.core.code.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.i18n.I18nSupport;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "core_code")
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeEntity extends BaseEntity implements I18nSupport<CodeI18nEntity> {
	
	@Id
	@Column(name = "code_id", length = 32)
	private String codeId;

    /**
     * Sets code name
     * @param name name
     */
    public void setName(String name) {
        i18nSet(i18n -> i18n.setName(name));
    }

    /**
     * Gets localized code name
     * @return code name
     */
    public String getName() {
        return i18nGet(CodeI18nEntity::getName);
    }

    @Column(name = "note", length = 4000)
	@Lob
	private String note;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(referencedColumnName = "code_id", name = "code_id", insertable = false, updatable = false)
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private List<CodeI18nEntity> i18ns = new ArrayList<>();

    @Override
    public CodeI18nEntity provideNewI18n(String locale) {
        return CodeI18nEntity.builder()
                .codeId(this.codeId)
                .locale(locale)
                .build();
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(referencedColumnName = "code_id", name = "code_id", insertable = false, updatable = false)
	@OrderBy(CodeItemEntity_.SORT)
	@Builder.Default
    @Setter(AccessLevel.NONE)
	private List<CodeItemEntity> itemEntities = new ArrayList<>();

}
