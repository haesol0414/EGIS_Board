package com.example.board.service.impl;

import com.example.board.dto.BoardDetailDTO;
import com.example.board.dto.BoardListDTO;
import com.example.board.dto.CreateBoardDTO;
import com.example.board.dto.UpdateBoardDTO;
import com.example.board.mapper.BoardMapper;
import com.example.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {
    private final BoardMapper boardMapper;

    @Autowired
    public BoardServiceImpl(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    // 게시글 작성
    public long createBoard(CreateBoardDTO createBoardDTO) {
        return 1;
    }

    // 게시글 목록 조회
    public List<BoardListDTO> getBoardList() {
        return boardMapper.getBoardList();
    }

    // 게시글 상세 조회
    public BoardDetailDTO getBoardDetail(Long boardNo) {
        return boardMapper.getBoardDetail(boardNo);
    }

    // 게시글 수정
    public long UpdateBoard(UpdateBoardDTO updateBoardDTO) {
        return 1;
    }

    // 게시글 삭제
    public long DeleteBoard(long boardId) {
        return 1;
    }
}
