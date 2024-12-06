package com.example.board.controller;

import com.example.board.dto.request.BoardCreateDTO;
import com.example.board.dto.request.BoardUpdateDTO;
import com.example.board.dto.request.BoardReplyDTO;
import com.example.board.dto.response.BoardDTO;
import com.example.board.dto.response.FileDTO;
import com.example.board.security.SecurityUtil;
import com.example.board.service.BoardService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.util.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class BoardRestController {
    private final BoardService boardService;
    private final SecurityUtil securityUtil;

    @Autowired
    public BoardRestController(BoardService boardService, SecurityUtil securityUtil) {
        this.boardService = boardService;
        this.securityUtil = securityUtil;
    }

    // 게시글 작성
    @PostMapping(value = "/board/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createBoard(
            @RequestPart("boardCreateDTO") BoardCreateDTO boardCreateDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            // 로그인 유저
            boardCreateDTO.setCreateUserId(securityUtil.getLoggedInUserId());

            // 공지사항 여부 체크
            if ("Y".equals(boardCreateDTO.getIsNotice())) {
                if (!securityUtil.isAdmin()) {
                    throw new SecurityException("공지사항 작성 권한이 없습니다.");
                }
                if (boardCreateDTO.getStartDate() != null && boardCreateDTO.getEndDate() != null) {
                    if (boardCreateDTO.getStartDate().after(boardCreateDTO.getEndDate())) {
                        throw new IllegalArgumentException("공지 종료일은 시작일보다 늦어야 합니다.");
                    }
                }
            }

            // 게시글 작성 서비스 호출 (공지사항 포함)
            Long boardNo = boardService.createBoard(boardCreateDTO, files);

            Map<String, Object> response = Map.of(
                    "message", "게시글 작성이 완료되었습니다.",
                    "boardNo", boardNo
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("게시글 작성 중 오류 발생", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "게시글 작성 중 에러가 발생했습니다: " + e.getMessage()));
        }
    }

    // 답글 작성
    @PostMapping(value = "/board/reply/{parentBoardNo}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> addReply(@PathVariable(name = "parentBoardNo") Long parentBoardNo,
                                                        @RequestPart("boardReplyDTO") BoardReplyDTO boardReplyDTO,
                                                        @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            // 현재 로그인 유저를 작성자로 설정
            boardReplyDTO.setCreateUserId(securityUtil.getLoggedInUserId());

            // 답글 작성 서비스 호출
            Long replyNo = boardService.addReply(parentBoardNo, boardReplyDTO, files);

            Map<String, Object> response = Map.of(
                    "message", "답글 작성이 완료되었습니다.",
                    "boardNo", replyNo
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("답글 작성 중 오류 발생: parentBoardNo = {}", parentBoardNo, e);

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
            // 게시글 작성자 확인
            BoardDTO board = boardService.getBoardDetail(boardNo);
            if (!board.getCreateUserId().equals(securityUtil.getLoggedInUserId()) && !securityUtil.isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("작성자 또는 관리자만 수정할 수 있습니다.");
            }

            if ("Y".equals(boardUpdateDTO.getIsNotice())) {
                if (!securityUtil.isAdmin()) {
                    throw new SecurityException("공지사항 작성 권한이 없습니다.");
                }
                if (boardUpdateDTO.getStartDate() != null && boardUpdateDTO.getEndDate() != null) {
                    if (boardUpdateDTO.getStartDate().after(boardUpdateDTO.getEndDate())) {
                        throw new IllegalArgumentException("공지 종료일은 시작일보다 늦어야 합니다.");
                    }
                }
            }

            boardUpdateDTO.setUpdateUserId(securityUtil.getLoggedInUserId());
            boardUpdateDTO.setUpdatedAt(new Date());

            // 업데이트 서비스 호출
            boardService.updateBoard(boardNo, boardUpdateDTO, files);

            return ResponseEntity.ok("게시글이 수정되었습니다.");
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
            // 게시글 작성자 확인
            BoardDTO board = boardService.getBoardDetail(boardNo);

            if (!board.getCreateUserId().equals(securityUtil.getLoggedInUserId()) && !securityUtil.isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("작성자 또는 관리자만 삭제할 수 있습니다.");
            }

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

            // 파일 삭제 여부 확인
            if ("Y".equals(fileDTO.getDeletedYn())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(null);
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
