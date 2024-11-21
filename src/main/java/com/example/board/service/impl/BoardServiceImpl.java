package com.example.board.service.impl;

import com.example.board.dto.response.BoardDetailDTO;
import com.example.board.dto.response.BoardListDTO;
import com.example.board.dto.request.BoardCreateDTO;
import com.example.board.dto.request.BoardUpdateDTO;
import com.example.board.mapper.BoardMapper;
import com.example.board.vo.BoardVO;
import com.example.board.service.BoardService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {
    private final BoardMapper boardMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public BoardServiceImpl(BoardMapper boardMapper, ModelMapper modelMapper) {
        this.boardMapper = boardMapper;
        this.modelMapper = modelMapper;
    }

    // 게시글 목록 조회 (페이징 처리)
    @Transactional(readOnly = true)
    public Map<String, Object> getBoardList(Pageable pageable) {
        int size = pageable.getPageSize(); // 한 페이지의 데이터 개수
        int offset = (pageable.getPageNumber() - 1) * size; // 시작 위치 (offset)

        // 페이징 처리된 게시글 목록 가져오기
        List<BoardVO> boards = boardMapper.getBoardList(size, offset);

        // 전체 페이지 수 계산
        int totalRecords = boardMapper.getTotalBoardCount();
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        // VO → DTO 변환
        List<BoardListDTO> boardList = boards.stream()
                .map(board -> modelMapper.map(board, BoardListDTO.class))
                .collect(Collectors.toList());

        // 결과 맵에 담아서 반환
        Map<String, Object> res = new HashMap<>();
        res.put("boardList", boardList);
        res.put("totalRecords", totalRecords);
        res.put("totalPages", totalPages);

        return res;
    }

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public BoardDetailDTO getBoardDetail(Long boardNo) {
        // VO 객체 조회
        BoardVO board = boardMapper.getBoardDetail(boardNo);

        // VO → DTO 변환
        return modelMapper.map(board, BoardDetailDTO.class);
    }

    // 게시글 작성
    @Transactional
    public void createBoard(BoardCreateDTO boardCreateDTO) {
        // DTO → VO 변환
        BoardVO newBoard = modelMapper.map(boardCreateDTO, BoardVO.class);

        // 데이터 삽입
        boardMapper.insertBoard(newBoard);
    }

    // 게시글 수정
    @Transactional
    public void updateBoard(BoardUpdateDTO boardUpdateDTO) {
        // DTO → VO 변환
        BoardVO updatedBoard = modelMapper.map(boardUpdateDTO, BoardVO.class);

        // 데이터 업데이트
        boardMapper.updateBoard(updatedBoard);
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
