package com.example.board.controller;

import com.example.board.dto.BoardCreateDTO;
import com.example.board.dto.BoardUpdateDTO;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.board.dto.BoardDetailDTO;
import com.example.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.Map;

@Controller
public class BoardController {
    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 게시글 목록 조회
    @GetMapping("/board/list")
    @ResponseBody
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

    // 게시글 상세 조회
    @GetMapping("/board/{boardNo}")
    public String getBoardDetail(@PathVariable(name = "boardNo") Long boardNo, Model model) {
        try {
            // 조회수 증가
            boardService.updateViewCnt(boardNo);

            // 상세 데이터 받아오기
            BoardDetailDTO boardDetail = boardService.getBoardDetail(boardNo);
            model.addAttribute("board", boardDetail);

            return "boardDetail";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "게시글을 불러오는 도중 문제가 발생했습니다.");

            return "error";
        }
    }

    // 게시글 작성
    @PostMapping("/board/write")
    public ResponseEntity<String> createBoard(@RequestBody BoardCreateDTO boardCreateDTO) {
        try {
            boardService.createBoard(boardCreateDTO);

            return ResponseEntity.ok("게시글 작성이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("게시글 작성 중 에러가 발생했습니다: " + e.getMessage());
        }
    }

    // 게시글 수정 폼 불러오기
    @GetMapping("/board/{boardNo}/edit")
    public String getBoardUpdateForm(@PathVariable(name = "boardNo") Long boardNo, Model model) {
        try {
            BoardDetailDTO boardDetail = boardService.getBoardDetail(boardNo);
            model.addAttribute("board", boardDetail);

            return "boardUpdate"; // 수정 폼 JSP
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "페이지를 불러오는 도중 문제가 발생했습니다.");

            return "error";
        }
    }

    // 게시글 수정
    @PatchMapping("/board/{boardNo}")
    public ResponseEntity<String> UpdateBoard(@PathVariable(name = "boardNo") Long boardNo, @RequestBody BoardUpdateDTO boardUpdateDTO) {
        try {
            boardService.updateBoard(boardUpdateDTO);

            return ResponseEntity.ok("수정이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("게시글 수정 중 에러가 발생했습니다: " + e.getMessage());
        }
    }

    // 게시글 삭제
    @DeleteMapping("/board/{boardNo}")
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
