package com.example.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.board.dto.BoardDetailDTO;
import com.example.board.dto.BoardListDTO;
import com.example.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<BoardListDTO> getBoardList() {
        List<BoardListDTO> boardList = boardService.getBoardList();

        return boardList;
    }

    // 게시글 상세 조회
    @GetMapping("/board/{boardNo}")
    public String getBoardDetail(@PathVariable(name = "boardNo") Long boardNo, Model model) {
        System.out.println(boardNo);
        BoardDetailDTO boardDetail = boardService.getBoardDetail(boardNo);
        System.out.println(boardDetail);
        model.addAttribute("board", boardDetail);

        return "boardDetail";
    }

    //    // 게시글 수정
    //    @PatchMapping("/board/{boardId}")
    //    public long UpdateBoard(UpdateBoardDTO updateBoardDTO) {
    //        return 1;
    //    }

    //    // 게시글 삭제
    //    @DeleteMapping("/board/{boardId}")
    //    public long DeleteBoard(long boardId) {
    //        return 1;
    //    }
}
