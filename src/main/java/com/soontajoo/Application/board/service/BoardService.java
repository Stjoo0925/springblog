package com.soontajoo.Application.board.service;

import com.soontajoo.Application.board.entity.BoardEntity;
import com.soontajoo.Application.board.repository.BoardRepository;
import com.soontajoo.Application.board.dto.BoardDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public boolean hasPermission(BoardDTO.Request boardRequest) {
        return false;
    }

    @Transactional
    public BoardEntity createBoard(BoardDTO.Request boardRequest) {
        // 제목의 중복을 확인하는 추가 검증 로직
        Optional<BoardEntity> existingBoard = boardRepository.findByTitle(boardRequest.getTitle());
        if (existingBoard.isPresent()) {
            throw new IllegalArgumentException("A board with the same title already exists");
        }

        // DTO로부터 엔티티를 생성합니다.
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setTitle(boardRequest.getTitle());
        boardEntity.setContent(boardRequest.getContent());
        boardEntity.setAuthor(boardRequest.getAuthor());

        // 엔티티를 데이터베이스에 저장합니다.
        return boardRepository.save(boardEntity);
    }

    @Transactional
    public BoardEntity updateBoard(BoardDTO.Request updateRequest) {
        Optional<BoardEntity> optionalBoard = boardRepository.findById(updateRequest.getBoardId());
        if (optionalBoard.isPresent()) {
            BoardEntity boardEntity = optionalBoard.get();
            boardEntity.setTitle(updateRequest.getTitle());
            boardEntity.setContent(updateRequest.getContent());
            return boardRepository.save(boardEntity); // 변경 사항을 저장합니다.
        }
        return null;
    }

    @Transactional
    public BoardEntity deleteBoard(BoardDTO.Request boardRequest) {
        return null;
    }

    @Transactional
    public Optional<BoardEntity> getAllBoards() {
        return null;
    }

    @Transactional
    public Optional<BoardEntity> getBoardById(int boardId) {
        return null;
    }
}
