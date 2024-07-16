package com.soontajoo.Application.board.controller;

import com.soontajoo.Application.board.entity.BoardEntity;
import com.soontajoo.Application.board.service.BoardService;
import com.soontajoo.Application.board.dto.BoardDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 게시판 생성
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createBoard(@Valid @ModelAttribute BoardDTO.Request boardRequest,
                                                           BindingResult bindingResult) {

        Map<String, String> response = new HashMap<>();

        /**
         *  - 유효성 검사 오류 처리
         * 1. bindingResult.hasErrors()
         * 이 메서드는 요청 객체(boardRequest)에 대한 유효성 검사가 실패했는지 확인합니다.
         * @Valid 어노테이션이 적용된 필드 중 하나라도 유효성 검사를 통과하지 못하면 true를 반환합니다.
         *
         * 2. bindingResult.getFieldErrors()
         * 유효성 검사 오류가 있는 경우, bindingResult 객체는 해당 오류 정보를 저장합니다.
         * getFieldErrors() 메서드는 모든 필드에 대한 오류 정보를 FieldError 객체의 리스트로 반환합니다.
         *
         * 3. 오류 정보를 응답 맵에 추가
         * 각 FieldError 객체는 필드 이름과 해당 필드의 오류 메시지를 포함합니다.
         * 이를 순회하면서 오류 메시지를 response 맵에 추가합니다. 이 맵은 클라이언트에 반환될 JSON 응답 본문을 구성합니다.
         *
         * 4. ResponseEntity 생성 및 반환
         * 유효성 검사 오류가 있을 경우, HTTP 상태 코드 400 (BAD_REQUEST)와 함께 response 맵을 응답 본문으로 반환합니다.
         * 이를 통해 클라이언트는 어떤 필드에 어떤 오류가 있는지 알 수 있습니다.
         *
         *  - HTTP 상태 코드
         * 1. BAD_REQUEST (HTTP 400)
         * 의미: 클라이언트의 요청이 잘못되었거나 유효하지 않음을 나타냅니다. 주로 클라이언트가 잘못된 데이터를 보냈을 때 사용됩니다.
         *
         * 2. FORBIDDEN (HTTP 403)
         * 의미: 클라이언트가 요청한 작업을 수행할 권한이 없음을 나타냅니다. 주로 인증은 되었으나 권한이 부족할 때 사용됩니다.
         */

        // 필수 필드 및 형식 검증
        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                response.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // 권한 확인
        if (!boardService.hasPermission(boardRequest)) {
            response.put("error", "게시물 생성 권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        BoardEntity updatedBoard = boardService.createBoard(boardRequest);
        if (updatedBoard == null) {
            response.put("error", "게시물 생성에 실패하였습니다");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("success", "게시물이 성공적으로 생성되었습니다");
        return ResponseEntity.ok(response);

    }

    // 게시판 수정
    @PutMapping("/update")
    public ResponseEntity<Map<String, String>> updateBoard(@Valid @ModelAttribute BoardDTO.Request boardRequest,
                                                           BindingResult bindingResult) {

        Map<String, String> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                response.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        BoardEntity updatedBoard = boardService.updateBoard(boardRequest);
        if (updatedBoard == null) {
            response.put("error", "게시물을 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("success", "게시물이 성공적으로 수정되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 게시판 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteBoard(@RequestBody BoardDTO.Request boardRequest,
                                                           BindingResult bindingResult) {
        Map<String, String> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                response.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        BoardEntity updatedBoard = boardService.deleteBoard(boardRequest);
        if (updatedBoard == null) {
            response.put("error", "게시물을 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("success", "게시물이 성공적으로 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }

    // 모든 게시물 조회
    @GetMapping("/get")
    public ResponseEntity<Map<String, Object>> getAllBoards() {
        Map<String, Object> response = new HashMap<>();

        Optional<BoardEntity> boards = boardService.getAllBoards();
        response.put("boards", boards);

        return ResponseEntity.ok(response);
    }

    // 게시물 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<Map<String, Object>> getBoardDetail(@Valid @ModelAttribute BoardDTO.Request boardRequest,
                                                              BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                response.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Optional<BoardEntity> board = boardService.getBoardById(boardRequest.getBoardId());
        if (board.isPresent()) {
            response.put("board", board.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "게시물을 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
