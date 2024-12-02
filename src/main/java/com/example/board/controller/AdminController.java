package com.example.board.controller;

import com.example.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final BoardService boardService;

    @Autowired
    public AdminController(BoardService boardService) {
        this.boardService = boardService;
    }

    // (관리자) 게시글 목록 조회 페이지
    @GetMapping("/board/list")
    public String getBoardList(
            @RequestParam(value = "filter", required = false, defaultValue = "subject") String filter,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "15") int size,
            Model model) {
        try {
            int offset = (page - 1) * size;

            // 목록 조회 서비스 호출
            Map<String, Object> boards = boardService.getBoardList(filter, keyword, size, offset);

            model.addAttribute("boardList", boards.get("boardList"));
            model.addAttribute("totalPages", boards.get("totalPages"));
            model.addAttribute("currentPage", page);
            model.addAttribute("filter", filter);
            model.addAttribute("keyword", keyword);

            return "adminBoardList";
        } catch (Exception e) {
            model.addAttribute("error", "게시글 목록 조회 중 오류가 발생했습니다.");
            return "error";
        }
    }
}
