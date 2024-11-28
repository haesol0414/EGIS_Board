package com.example.board.controller;

import com.example.board.dto.request.BoardCreateDTO;
import com.example.board.dto.request.BoardUpdateDTO;
import com.example.board.dto.request.BoardReplyDTO;
import com.example.board.dto.response.FileDTO;
import com.example.board.service.BoardService;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.file.Files;
import java.io.File;


import java.io.File;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class BoardRestController {
    private final BoardService boardService;

    @Autowired
    public BoardRestController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 게시글 목록 조회
    @GetMapping("/board/list")
    public ResponseEntity<Map<String, Object>> getBoardList(
            @RequestParam(value = "filter", required = false) String filter,
            @RequestParam(value = "keyword", required = false) String keyword,
            @PageableDefault(size = 15) Pageable pageable
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 목록 조회 서비스 호출
            Map<String, Object> boards = boardService.getBoardList(filter, keyword, pageable);

            response.put("status", "success");
            response.put("data", boards);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("게시글 목록 조회 중 오류 발생", e);
            response.put("status", "error");
            response.put("message", "게시글 목록 조회 중 오류가 발생했습니다.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 게시글 작성
    @PostMapping(value = "/board/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createBoard(
            @RequestPart("boardCreateDTO") BoardCreateDTO boardCreateDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            // 로그인 유저
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boardCreateDTO.setCreateUserId(authentication.getName());

            // 게시글 작성 서비스 호출
            Long boardNo = boardService.createBoard(boardCreateDTO, files);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "게시글 작성이 완료되었습니다.");
            response.put("boardNo", boardNo);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("게시글 작성 중 오류 발생", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "게시글 작성 중 에러가 발생했습니다: " + e.getMessage()));
        }
    }

    // 답글 작성
    @PostMapping(value = "/board/reply/{boardNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> addReply(@PathVariable(name = "boardNo") Long boardNo,
                                                        @RequestPart("boardReplyDTO") BoardReplyDTO boardReplyDTO,
                                                        @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            // 로그인 유저
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boardReplyDTO.setCreateUserId(authentication.getName());

            // 답글 작성 서비스 호출
            Long replyNo = boardService.addReply(boardNo, boardReplyDTO, files);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "답글 작성이 완료되었습니다.");
            response.put("boardNo", replyNo);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("답글 작성 중 오류 발생: boardNo = {}", boardNo, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "답글 작성 중 에러가 발생했습니다: " + e.getMessage()));
        }
    }

    // 게시글 수정
    @PatchMapping(value = "/board/{boardNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateBoard(@PathVariable(name = "boardNo") Long boardNo,
                                              @RequestPart("boardUpdateDTO") BoardUpdateDTO boardUpdateDTO,
                                              @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            // 로그인 유저
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boardUpdateDTO.setUpdateUserId(authentication.getName());

            // 업데이트 서비스 호출
            boardService.updateBoard(boardNo, boardUpdateDTO, files);

            return ResponseEntity.ok("수정이 완료되었습니다.");
        } catch (Exception e) {
            log.error("게시글 수정 중 오류 발생: boardNo = {}", boardNo, e);

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
            log.error("게시글 삭제 중 오류 발생: boardNo = {}", boardNo, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("게시글 삭제 중 에러가 발생했습니다: " + e.getMessage());
        }
    }

    // 파일 다운로드
    @GetMapping("/files/download/{attachmentId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable(name = "attachmentId") Long attachmentId) {
        try {
            // 파일 상세 정보 조회
            FileDTO fileDTO = boardService.getFileDetails(attachmentId);

            // 파일 객체 생성
            File file = new File(fileDTO.getFilePath());
            if (!file.exists() || !file.canRead()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            String mimeType = Files.probeContentType(file.toPath()) != null
                    ? Files.probeContentType(file.toPath())
                    : "application/octet-stream";

            // 원본 파일 이름 인코딩
            String encodedFileName = URLEncoder.encode(fileDTO.getOriginFileName(), "UTF-8").replace("+", "%20");

            // 파일 리소스 생성
            Resource resource = new FileSystemResource(file);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                    .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                    .header(HttpHeaders.PRAGMA, "no-cache")
                    .header(HttpHeaders.EXPIRES, "0")
                    .body(resource);

        } catch (Exception e) {
            log.error("파일 다운로드 중 오류 발생", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
