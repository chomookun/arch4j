package org.oopscraft.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.repository.BoardEntity;
import org.oopscraft.arch4j.core.board.repository.BoardRepository;
import org.oopscraft.arch4j.core.board.repository.BoardSpecification;
import org.oopscraft.arch4j.core.role.repository.RoleEntity;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    /**
     * saves board info
     * @param board board info
     * @return board
     */
    public Board saveBoard(Board board) {
        BoardEntity boardEntity = boardRepository.findById(board.getBoardId()).orElse(null);
        if(boardEntity == null) {
            boardEntity = BoardEntity.builder()
                    .boardId(board.getBoardId())
                    .build();
        }
        boardEntity.setName(board.getName());
        boardEntity.setNote(board.getNote());
        boardEntity.setIcon(board.getIcon());
        boardEntity.setMessageFormat(board.getMessageFormat());
        boardEntity.setMessage(board.getMessage());
        boardEntity.setSkin(board.getSkin());
        boardEntity.setPageSize(board.getPageSize());
        boardEntity.setFileEnabled(board.isFileEnabled());
        boardEntity.setAccessPolicy(board.getAccessPolicy());
        boardEntity.setAccessRoles(board.getAccessRoles().stream()
                .map(role -> RoleEntity.builder()
                        .roleId(role.getRoleId())
                        .build())
                .collect(Collectors.toList()));
        boardEntity.setReadPolicy(board.getReadPolicy());
        boardEntity.setReadRoles(board.getReadRoles().stream()
                .map(role -> RoleEntity.builder()
                        .roleId(role.getRoleId())
                        .build())
                .collect(Collectors.toList()));
        boardEntity.setWritePolicy(board.getWritePolicy());
        boardEntity.setWriteRoles(board.getWriteRoles().stream()
                .map(role -> RoleEntity.builder()
                        .roleId(role.getRoleId())
                        .build())
                .collect(Collectors.toList()));
        boardEntity.setCommentEnabled(board.isCommentEnabled());
        boardEntity.setCommentPolicy(board.getCommentPolicy());
        boardEntity.setCommentRoles(board.getCommentRoles().stream()
                .map(role -> RoleEntity.builder()
                        .roleId(role.getRoleId())
                        .build())
                .collect(Collectors.toList()));

        // save
        boardEntity = boardRepository.saveAndFlush(boardEntity);
        return Board.from(boardEntity);
    }

    /**
     * returns board
     * @param boardId board id
     * @return board info
     */
    public Optional<Board> getBoard(String boardId) {
        return boardRepository.findById(boardId)
                .map(Board::from);
    }

    /**
     * deletes board
     * @param boardId board id
     */
    public void deleteBoard(String boardId) {
        boardRepository.deleteById(boardId);
        boardRepository.flush();
    }

    /**
     * return list of board
     * @param boardSearch board search condition
     * @param pageable pagination info
     * @return board list
     */
    public Page<Board> getBoards(BoardSearch boardSearch, Pageable pageable) {
        Specification<BoardEntity> specification = (root, query, criteriaBuilder) -> null;
        if(boardSearch.getBoardId() != null) {
            specification = specification.and(BoardSpecification.likeBoardId(boardSearch.getBoardId()));
        }
        if(boardSearch.getName() != null) {
            specification = specification.and(BoardSpecification.likeName(boardSearch.getName()));
        }
        Page<BoardEntity> boardEntityPage = boardRepository.findAll(specification, pageable);
        List<Board> boards = boardEntityPage.getContent().stream()
                .map(Board::from)
                .collect(Collectors.toList());
        long total = boardEntityPage.getTotalElements();
        return new PageImpl<>(boards, pageable, total);
    }

    public void checkAccessPermission(Board board) {
        SecurityUtils.checkPermission(board.getAccessPolicy(), board.getAccessRoles());
    }

    public void checkReadPermission(Board board) {
        SecurityUtils.checkPermission(board.getReadPolicy(), board.getReadRoles());
    }

    public void checkWritePermission(Board board) {
        SecurityUtils.checkPermission(board.getWritePolicy(), board.getWriteRoles());
    }

    public void checkCommentPermission(Board board) {
        SecurityUtils.checkPermission(board.getCommentPolicy(), board.getCommentRoles());
    }

}
