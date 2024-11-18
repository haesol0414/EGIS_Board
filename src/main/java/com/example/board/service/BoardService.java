package com.example.board.service;

import com.example.board.dto.*;

import java.util.List;

public interface BoardService {
    // 게시글 작성
    long createBoard(CreateBoardDTO createBoardDTO);
    // 게시글 목록 조회
    List<BoardListDTO> getBoardList();
    // 게시글 상세 조회
    BoardDetailDTO getBoardDetail(Long boardNo);
    // 게시글 수정
    long UpdateBoard(UpdateBoardDTO updateBoardDTO);
    // 게시글 삭제
    long DeleteBoard(long boardId);
}
