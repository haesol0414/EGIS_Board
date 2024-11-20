package com.example.board.controller;

import com.example.board.dto.BoardDetailDTO;
import com.example.board.service.BoardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 게시글 작성 페이지
    @GetMapping("/write")
    public String showWritePage() {
        return "boardWrite";
    }

    // 게시글 상세 페이지
    @GetMapping("/{boardNo}")
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

    // 게시글 수정 페이지
    @GetMapping("/{boardNo}/edit")
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
}
