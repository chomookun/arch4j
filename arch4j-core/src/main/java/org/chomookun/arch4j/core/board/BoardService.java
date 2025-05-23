package org.chomookun.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.board.entity.BoardEntity;
import org.chomookun.arch4j.core.board.repository.BoardRepository;
import org.chomookun.arch4j.core.board.entity.BoardRoleEntity;
import org.chomookun.arch4j.core.board.model.Board;
import org.chomookun.arch4j.core.board.model.BoardSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Board saveBoard(Board board) {
        BoardEntity boardEntity = boardRepository.findById(board.getBoardId())
                .orElse(BoardEntity.builder()
                    .boardId(board.getBoardId())
                    .build());
        boardEntity.setSystemUpdatedAt(LocalDateTime.now());    // disable dirty checking
        boardEntity.setBoardName(board.getName());
        boardEntity.setIcon(board.getIcon());
        boardEntity.setMessageFormat(board.getMessageFormat());
        boardEntity.setMessage(board.getMessage());
        boardEntity.setSkin(board.getSkin());
        boardEntity.setPageSize(board.getPageSize());
        boardEntity.setFileEnabled(board.isFileEnabled());
        boardEntity.setStorageId(board.getStorageId());
        boardEntity.setFileSizeLimit(board.getFileSizeLimit());
        boardEntity.setDiscussionEnabled(board.isDiscussionEnabled());
        boardEntity.setDiscussionId(board.getDiscussionId());
        // access roles
        boardEntity.getBoardRoleEntities().clear();
        board.getAccessRoles().forEach(accessRole -> {
            BoardRoleEntity boardRoleEntity = BoardRoleEntity.builder()
                    .boardId(boardEntity.getBoardId())
                    .roleId(accessRole.getRoleId())
                    .type("ACCESS")
                    .build();
            boardEntity.getBoardRoleEntities().add(boardRoleEntity);
        });
        // read roles
        boardEntity.getBoardRoleEntities().clear();
        board.getReadRoles().forEach(readRole -> {
            BoardRoleEntity boardRoleEntity = BoardRoleEntity.builder()
                    .boardId(boardEntity.getBoardId())
                    .roleId(readRole.getRoleId())
                    .type("READ")
                    .build();
            boardEntity.getBoardRoleEntities().add(boardRoleEntity);
        });
        // write roles
        boardEntity.getBoardRoleEntities().clear();
        board.getWriteRoles().forEach(writeRole -> {
            BoardRoleEntity boardRoleEntity = BoardRoleEntity.builder()
                    .boardId(boardEntity.getBoardId())
                    .roleId(writeRole.getRoleId())
                    .type("WRITE")
                    .build();
            boardEntity.getBoardRoleEntities().add(boardRoleEntity);
        });
        // save
        BoardEntity savedBoardEntity = boardRepository.saveAndFlush(boardEntity);
        // return
        return this.getBoard(savedBoardEntity.getBoardId()).orElseThrow();
    }

    public Optional<Board> getBoard(String boardId) {
        return boardRepository.findById(boardId)
                .map(Board::from);
    }

    public void deleteBoard(String boardId) {
        boardRepository.deleteById(boardId);
        boardRepository.flush();
    }

    public Page<Board> getBoards(BoardSearch boardSearch, Pageable pageable) {
        Page<BoardEntity> boardEntityPage = boardRepository.findAll(boardSearch, pageable);
        List<Board> boards = boardEntityPage.getContent().stream()
                .map(Board::from)
                .collect(Collectors.toList());
        long total = boardEntityPage.getTotalElements();
        return new PageImpl<>(boards, pageable, total);
    }

}
