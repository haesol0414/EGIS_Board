package com.example.board.controller;

import com.example.board.dto.response.BoardDTO;
import com.example.board.service.AdminService;
import com.example.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final BoardService boardService;

    @Autowired
    public AdminController(AdminService adminService, BoardService boardService) {
        this.adminService = adminService;
        this.boardService = boardService;
    }

    // (관리자) 게시글 목록 조회 페이지
    @GetMapping("/board")
    public String getAdminBoardList(
            @RequestParam(value = "filter", required = false, defaultValue = "subject") String filter,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "15") int size,
            Model model) {
        try {
            int offset = (page - 1) * size;

            // 공지사항 조회 서비스 호출
            List<BoardDTO> noticeList = boardService.getNoticeList();

            if (!noticeList.isEmpty()) {
                model.addAttribute("noticeList", noticeList); // 공지사항 추가
            }

            // 목록 조회 서비스 호출
            Map<String, Object> boards = adminService.getAdminBoardList(filter, keyword, size, offset);

            model.addAllAttributes(Map.of(
                    "boardList", boards.get("boardList"),
                    "totalPages", boards.get("totalPages"),
                    "currentPage", page,
                    "filter", filter,
                    "keyword", keyword
            ));

            return "adminBoardList";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "게시글 목록 조회 중 오류가 발생했습니다.");
            return "error";
        }
    }
}
