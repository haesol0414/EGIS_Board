package com.example.board.controller;

import com.example.board.dto.request.BoardCreateDTO;
import com.example.board.dto.request.BoardUpdateDTO;
import com.example.board.service.BoardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public Map<String, Object> getBoardList(@PageableDefault(size = 15) Pageable pageable) {
        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> boards = boardService.getBoardList(pageable);

            response.put("status", "success");
            response.put("data", boards);
        } catch (Exception e) {
            e.printStackTrace();

            response.put("status", "error");
            response.put("message", "게시글 목록 조회 중 오류가 발생했습니다.");
        }

        return response;
    }

    // 게시글 작성
    @PostMapping("/write")
    public ResponseEntity<String> createBoard(@RequestBody BoardCreateDTO boardCreateDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String createUserId = authentication.getName();
            // 사용자 ID 설정
            boardCreateDTO.setCreateUserId(createUserId);

            // 게시글 작성 서비스 호출
            boardService.createBoard(boardCreateDTO);

            return ResponseEntity.ok("게시글 작성이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("게시글 작성 중 에러가 발생했습니다: " + e.getMessage());
        }
    }

    // 게시글 수정
    @PatchMapping("/{boardNo}")
    public ResponseEntity<String> UpdateBoard(@PathVariable(name = "boardNo") Long boardNo, @RequestBody BoardUpdateDTO boardUpdateDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String createUserId = authentication.getName();
            // 사용자 ID 설정
            boardUpdateDTO.setUpdateUserId(createUserId);

            // 업데이트 서비스 호출
            boardService.updateBoard(boardUpdateDTO);

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
