package com.example.board.service.impl;

import com.example.board.dto.BoardDetailDTO;
import com.example.board.dto.BoardListDTO;
import com.example.board.dto.BoardCreateDTO;
import com.example.board.dto.BoardUpdateDTO;
import com.example.board.mapper.BoardMapper;
import com.example.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardServiceImpl implements BoardService {
    private final BoardMapper boardMapper;

    @Autowired
    public BoardServiceImpl(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    // 게시글 목록 조회 (페이징 처리)
    public Map<String, Object> getBoardList(Pageable pageable) {
        int size = pageable.getPageSize(); // 한 페이지의 데이터 개수
        int offset = (pageable.getPageNumber() - 1) * size; // 시작 위치 (offset)

        // 페이징 처리된 게시글 목록 가져오기
        List<BoardListDTO> boardList = boardMapper.getBoardList(size, offset);

        // 전체 페이지 수 계산
        int totalRecords = boardMapper.getTotalBoardCount();
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        // 결과 맵에 담아서 반환
        Map<String, Object> boards = new HashMap<>();
        boards.put("boardList", boardList);
        boards.put("totalRecords", totalRecords);
        boards.put("totalPages", totalPages);

        return boards;
    }

    // 게시글 상세 조회
    public BoardDetailDTO getBoardDetail(Long boardNo) {
        return boardMapper.getBoardDetail(boardNo);
    }

    // 게시글 작성
    @Transactional
    public void createBoard(BoardCreateDTO boardCreateDTO) {
        boardMapper.insertBoard(boardCreateDTO);
    }

    // 게시글 수정
    @Transactional
    public void updateBoard(BoardUpdateDTO boardUpdateDTO) {
        boardMapper.updateBoard(boardUpdateDTO);
    }

    // 게시글 삭제
    @Transactional
    public void deleteBoard(Long boardNo) {
        boardMapper.deleteBoard(boardNo);
    }

    // 조회수 증가
    @Transactional
    public void updateViewCnt(Long boardNo) {
        boardMapper.updateViewCnt(boardNo);
    }
}
