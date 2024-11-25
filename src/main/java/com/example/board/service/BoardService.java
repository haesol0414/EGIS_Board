package com.example.board.service;

import com.example.board.dto.request.BoardCreateDTO;
import com.example.board.dto.request.BoardUpdateDTO;
import com.example.board.dto.request.BoardReplyDTO;
import com.example.board.dto.response.BoardDetailDTO;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface BoardService {
    // 게시글 작성
    void createBoard(BoardCreateDTO boardCreateDTO);
    // 게시글 목록 조회
    Map<String, Object> getBoardList(String filter, String keyword, Pageable pageable);
    // 게시글 상세 조회
    BoardDetailDTO getBoardDetail(Long boardNo);
    // 게시글 수정
    void updateBoard(BoardUpdateDTO boardUpdateDTO);
    // 게시글 삭제
    void deleteBoard(Long boardNo);
    // 조회수 증가
    void updateViewCnt(Long boardNo);
    // 답글 작성
    void addReply(Long boardNo, BoardReplyDTO boardReplyDTO);
}
