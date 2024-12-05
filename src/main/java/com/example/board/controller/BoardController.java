package com.example.board.controller;

import com.example.board.dto.response.BoardDTO;
import com.example.board.dto.response.FileDTO;
import com.example.board.security.SecurityUtil;
import com.example.board.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/board")
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final SecurityUtil securityUtil;

    @Autowired
    public BoardController(BoardService boardService, SecurityUtil securityUtil) {
        this.boardService = boardService;
        this.securityUtil = securityUtil;
    }

    // 게시글 목록 조회 페이지
    @GetMapping
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

            return "boardList";
        } catch (Exception e) {
            model.addAttribute("error", "게시글 목록 조회 중 오류가 발생했습니다.");
            return "error";
        }
    }

    // 게시글 작성 페이지
    @GetMapping("/write")
    public String showWritePage(Model model) {
        try {
            if (securityUtil.isAdmin()) {
                model.addAttribute("isAdmin", true);
            }

            return "boardWrite";
        } catch (Exception e) {
            log.error("게시글 작성 페이지 로드 중 오류 발생", e);
            model.addAttribute("errorMessage", "게시글 작성 페이지를 불러오는 도중 문제가 발생했습니다.");

            return "error";
        }
    }

    // 답글 작성 페이지
    @GetMapping("/reply/{boardNo}")
    public String showWriteReplyPage(@PathVariable(name = "boardNo") Long boardNo, Model model) {
        try {
            BoardDTO parent = boardService.getBoardDetail(boardNo);
            model.addAttribute("parent", parent);

            return "boardWrite";
        } catch (Exception e) {
            log.error("답글 작성 페이지 로드 중 오류 발생: boardNo = {}", boardNo, e);
            model.addAttribute("errorMessage", "답글 작성 페이지를 불러오는 도중 문제가 발생했습니다.");

            return "error";
        }
    }

    // 게시글 상세 페이지
    @GetMapping("/{boardNo}")
    public String getBoardDetail(@PathVariable(name = "boardNo") Long boardNo,
                                 @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                 @RequestParam(name = "filter", required = false, defaultValue = "subject") String filter,
                                 @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                                 Model model) {
        try {
            // 게시글 상세 데이터 가져오기
            BoardDTO boardDetail = boardService.getBoardDetail(boardNo);

            System.out.println(securityUtil.isAdmin());
            // 삭제된 게시글 처리
            if ("Y".equals(boardDetail.getDeletedYn()) && !securityUtil.isAdmin()) {
                model.addAttribute("errorMessage", "삭제된 게시글입니다.");
                return "error";
            } else {
                // 조회수 증가 (삭제되지 않은 경우에만)
                boardService.updateViewCnt(boardNo);
            }

            // 게시글 정보 추가
            model.addAttribute("board", boardDetail);
            model.addAttribute("isAdmin", securityUtil.isAdmin()); // 관리자 여부 추가

            // 파일 데이터 가져오기
            List<FileDTO> files = boardService.getFilesByBoardNo(boardNo);
            if (files != null) {
                model.addAttribute("files", files);
            }

            // 페이지 및 검색 정보 추가
            model.addAttribute("currentPage", page);
            model.addAttribute("filter", filter);
            model.addAttribute("keyword", keyword);

            return "boardDetail";
        } catch (Exception e) {
            log.error("게시글 상세 페이지 로드 중 오류 발생: boardNo = {}", boardNo, e);
            model.addAttribute("errorMessage", "존재하지 않는 게시글입니다.");
            return "error";
        }
    }

    // 게시글 수정 페이지
    @GetMapping("/edit/{boardNo}")
    public String getBoardUpdateForm(@PathVariable(name = "boardNo") Long boardNo, Model model) {
        try {
            // 기존 게시글 불러오기
            BoardDTO boardDetail = boardService.getBoardDetail(boardNo);
            model.addAttribute("board", boardDetail);

            // 첨부파일 불러오기
            List<FileDTO> files = boardService.getFilesByBoardNo(boardNo);
            if (files != null) {
                model.addAttribute("files", files);
            }

            return "boardUpdate"; // 수정 폼 JSP
        } catch (Exception e) {
            log.error("게시글 수정 페이지 로드 중 오류 발생: boardNo = {}", boardNo, e);
            model.addAttribute("errorMessage", "페이지를 불러오는 도중 문제가 발생했습니다.");

            return "error";
        }
    }
}
