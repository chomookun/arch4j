package org.chomookun.arch4j.core.code.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.i18n.I18nGetter;
import org.chomookun.arch4j.core.common.i18n.I18nSetter;
import org.chomookun.arch4j.core.common.i18n.I18nSupportEntity;

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
public class CodeEntity extends BaseEntity implements I18nSupportEntity<CodeI18nEntity> {
	
	@Id
	@Column(name = "code_id", length = 32)
	private String codeId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description", length = 4000)
	@Lob
	private String description;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(referencedColumnName = "code_id", name = "code_id", insertable = false, updatable = false)
	@OrderBy(CodeItemEntity_.SORT)
	@Builder.Default
    @Setter(AccessLevel.NONE)
	private List<CodeItemEntity> codeItemEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(referencedColumnName = "code_id", name = "code_id", insertable = false, updatable = false)
    @Builder.Default
    @Setter(AccessLevel.NONE)
    private List<CodeI18nEntity> codeI18nEntities = new ArrayList<>();

    @Override
    public List<CodeI18nEntity> provideI18nEntities() {
        return this.codeI18nEntities;
    }

    @Override
    public CodeI18nEntity provideNewI18nEntity(String language) {
        return CodeI18nEntity.builder()
                .codeId(this.codeId)
                .language(language)
                .build();
    }

    public void setName(String codeName) {
        I18nSetter.of(this, this.name)
                .whenDefault(() -> this.name = codeName)
                .whenI18n(codeLanguageEntity -> codeLanguageEntity.setName(codeName))
                .set();
    }

    public String getName() {
        return I18nGetter.of(this, this.name)
                .whenDefault(() -> this.name)
                .whenI18n(CodeI18nEntity::getName)
                .get();
    }

}
