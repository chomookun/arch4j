package org.chomookun.arch4j.core.board.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.board.model.Board;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.i18n.I18nGetter;
import org.chomookun.arch4j.core.common.i18n.I18nSetter;
import org.chomookun.arch4j.core.common.i18n.I18nSupportEntity;
import org.chomookun.arch4j.core.common.data.converter.GenericEnumConverter;
import org.chomookun.arch4j.core.common.data.converter.BooleanConverter;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "core_board")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardEntity extends BaseEntity implements I18nSupportEntity<BoardI18nEntity> {

    @Id
    @Column(name = "board_id", length = 64)
    private String boardId;

    @Column(name = "board_name")
    private String boardName;

    @Column(name = "icon", length = 4000)
    @Lob
    private String icon;

    @Column(name = "message", length = 4000)
    @Lob
    private String message;

    @Column(name = "message_format", length = 16)
    private Board.MessageFormat messageFormat;

    @Column(name = "skin")
    private String skin;

    @Column(name = "page_size")
    private Integer pageSize;

    @Column(name = "file_enabled", length = 1)
    @Convert(converter= BooleanConverter.class)
    private boolean fileEnabled;

    @Column(name = "storage_id", length = 32)
    private String storageId;

    @Column(name = "file_size_limit")
    private Integer fileSizeLimit;

    @Column(name = "discussion_enabled", length = 1)
    @Convert(converter= BooleanConverter.class)
    private boolean discussionEnabled;

    @Column(name = "discussion_id", length = 32)
    private String discussionId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Builder.Default
    private List<BoardRoleEntity> boardRoleEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id", updatable = false)
    @Builder.Default
    private List<BoardI18nEntity> boardI18nEntities = new ArrayList<>();

    @Override
    public List<BoardI18nEntity> provideI18nEntities() {
        return this.boardI18nEntities;
    }

    @Override
    public BoardI18nEntity provideNewI18nEntity(String language) {
        return BoardI18nEntity.builder()
                .boardId(this.boardId)
                .language(language)
                .build();
    }

    public void setBoardName(String name) {
        I18nSetter.of(this, this.boardName)
                .whenDefault(() -> this.boardName = name)
                .whenI18n(i18nEntity -> i18nEntity.setBoardName(name))
                .set();
    }

    public String getBoardName() {
        return I18nGetter.of(this, this.boardName)
                .whenDefault(() -> this.boardName)
                .whenI18n(BoardI18nEntity::getBoardName)
                .get();
    }

    public void setMessage(String message) {
        I18nSetter.of(this, this.message)
                .whenDefault(() -> this.message = message)
                .whenI18n(i18nEntity -> i18nEntity.setMessage(message))
                .set();
    }

    public String getMessage() {
        return I18nGetter.of(this, this.message)
                .whenDefault(() -> this.message)
                .whenI18n(BoardI18nEntity::getMessage)
                .get();
    }

    @Converter(autoApply = true)
    public static class MessageFormatConverter extends GenericEnumConverter<Board.MessageFormat> {}

}
