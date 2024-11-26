package com.example.board.controller;

import com.example.board.dto.request.BoardCreateDTO;
import com.example.board.dto.request.BoardUpdateDTO;
import com.example.board.dto.request.BoardReplyDTO;
import com.example.board.service.BoardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/board")
public class BoardRestController {
    private final BoardService boardService;

    @Autowired
    public BoardRestController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 게시글 목록 조회
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getBoardList(
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "keyword", required = false) String keyword,
            @PageableDefault(size = 15) Pageable pageable
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 서비스 호출
            Map<String, Object> boards = boardService.getBoardList(filter, keyword, pageable);

            response.put("status", "success");
            response.put("data", boards);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "게시글 목록 조회 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 게시글 작성
    @PostMapping(value = "/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createBoard(
            @RequestPart("boardCreateDTO") BoardCreateDTO boardCreateDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boardCreateDTO.setCreateUserId(authentication.getName());

            // 게시글 작성 서비스 호출
            Long boardNo = boardService.createBoard(boardCreateDTO, file);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "게시글 작성이 완료되었습니다.");
            response.put("boardNo", boardNo);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "게시글 작성 중 에러가 발생했습니다: " + e.getMessage()));
        }
    }

    // 답글 작성
    @PostMapping(value = "/reply/{boardNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>>  addReply(@PathVariable(name = "boardNo") Long boardNo,
            @RequestPart("boardReplyDTO") BoardReplyDTO boardReplyDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boardReplyDTO.setCreateUserId(authentication.getName());

            // 답글 작성 서비스 호출
            Long replyNo = boardService.addReply(boardNo, boardReplyDTO, file);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "답글 작성이 완료되었습니다.");
            response.put("boardNo", replyNo);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "답글 작성 중 에러가 발생했습니다: " + e.getMessage()));
        }
    }


    // 게시글 수정
    @PatchMapping(value = "/{boardNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> UpdateBoard(@PathVariable(name = "boardNo") Long boardNo, @RequestPart("boardUpdateDTO") BoardUpdateDTO boardUpdateDTO,
                                              @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boardUpdateDTO.setUpdateUserId(authentication.getName());

            // 업데이트 서비스 호출
            boardService.updateBoard(boardNo, boardUpdateDTO, file);

            return ResponseEntity.ok("수정이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("게시글 수정 중 에러가 발생했습니다: " + e.getMessage());
        }
    }

    // 게시글 삭제
    @DeleteMapping("/{boardNo}")
    public ResponseEntity<String> deleteBoard(@PathVariable(name = "boardNo") Long boardNo) {
        try {
            boardService.deleteBoard(boardNo);

            return ResponseEntity.ok("삭제 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("게시글 삭제 중 에러가 발생했습니다: " + e.getMessage());
        }
    }
}
