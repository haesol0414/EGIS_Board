package com.example.board.service.impl;

import com.example.board.dto.request.BoardReplyDTO;
import com.example.board.dto.response.BoardDetailDTO;
import com.example.board.dto.request.BoardCreateDTO;
import com.example.board.dto.request.BoardUpdateDTO;
import com.example.board.mapper.BoardMapper;
import com.example.board.vo.BoardVO;
import com.example.board.service.BoardService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
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
    private final ModelMapper modelMapper;

    @Autowired
    public BoardServiceImpl(BoardMapper boardMapper, ModelMapper modelMapper) {
        this.boardMapper = boardMapper;
        this.modelMapper = modelMapper;
    }

    // 게시글 목록 조회 (페이징 처리)
    public Map<String, Object> getBoardList(String filter, String keyword, Pageable pageable) {
        int size = pageable.getPageSize();
        int offset = (pageable.getPageNumber() - 1) * size;

        List<BoardVO> boards;
        int totalRecords;

        if (filter != null && keyword != null && !keyword.isBlank()) {
            // 검색 조건이 있는 경우
            boards = boardMapper.searchBoardList(filter, keyword, size, offset);
            totalRecords = boardMapper.selectSearchTotalCount(filter, keyword);
        } else {
            // 검색 조건이 없는 경우
            boards = boardMapper.selectBoardList(size, offset);
            totalRecords = boardMapper.selectBoardTotalCount();
        }

        int totalPages = (int) Math.ceil((double) totalRecords / size);

        Map<String, Object> result = new HashMap<>();
        result.put("boardList", boards);
        result.put("totalRecords", totalRecords);
        result.put("totalPages", totalPages);

        return result;
    }

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public BoardDetailDTO getBoardDetail(Long boardNo) {
        // VO 객체 조회
        BoardVO board = boardMapper.selectBoardDetail(boardNo);

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

    // 답글 작성
    @Transactional
    public void addReply(Long boardNo, BoardReplyDTO boardReplyDTO) {
        // 부모 글 정보 조회
        BoardVO parent = boardMapper.selectBoardDetail(boardNo);
        if (parent == null) {
            throw new IllegalArgumentException("부모 글이 존재하지 않습니다.");
        }

        // 그룹 정보 설정
        boardReplyDTO.setGroupNo(parent.getGroupNo());                     // 부모 글의 그룹 번호 상속
        boardReplyDTO.setGroupOrd(parent.getGroupOrd() + 1);               // 부모 글의 다음 순서
        boardReplyDTO.setGroupDep(parent.getGroupDep() + 1);               // 부모 글의 깊이 + 1

        // 기존 글 순서 밀기
        boardMapper.updateGroupOrd(parent.getGroupNo(), parent.getGroupOrd());

        BoardVO newReply = modelMapper.map(boardReplyDTO, BoardVO.class);

        System.out.println(newReply);
        boardMapper.insertReply(newReply);
    }
}
