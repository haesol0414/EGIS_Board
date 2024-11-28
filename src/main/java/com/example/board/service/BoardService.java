package com.example.board.service;

import com.example.board.dto.request.BoardCreateDTO;
import com.example.board.dto.request.BoardUpdateDTO;
import com.example.board.dto.request.BoardReplyDTO;
import com.example.board.dto.response.BoardDetailDTO;
import com.example.board.dto.response.FileDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BoardService {
    // 게시글 작성
    Long createBoard(BoardCreateDTO boardCreateDTO, List<MultipartFile> files);
    // 게시글 목록 조회
    Map<String, Object> getBoardList(String filter, String keyword, Pageable pageable);
    // 게시글 상세 조회
    BoardDetailDTO getBoardDetail(Long boardNo);
    // 게시글 수정
    void updateBoard(Long boardNo, BoardUpdateDTO boardUpdateDTO, List<MultipartFile> files);
    // 게시글 삭제
    void deleteBoard(Long boardNo);
    // 조회수 증가
    void updateViewCnt(Long boardNo);
    // 답글 작성
    Long addReply(Long boardNo, BoardReplyDTO boardReplyDTO, List<MultipartFile> files);
    // 파일 저장
    void uploadFiles(List<MultipartFile> files, Long boardNo);
    // 게시글 내 파일 조회
    List<FileDTO> getFilesByBoardNo(Long boardNo);
    // 파일 상세 조회
    FileDTO getFileDetails(Long attachmentId);
}