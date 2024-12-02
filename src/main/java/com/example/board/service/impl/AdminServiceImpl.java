package com.example.board.service.impl;

import com.example.board.dto.response.BoardDTO;
import com.example.board.mapper.BoardMapper;
import com.example.board.service.AdminService;
import com.example.board.vo.BoardVO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    private final BoardMapper boardMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public AdminServiceImpl(BoardMapper boardMapper, ModelMapper modelMapper) {
        this.boardMapper = boardMapper;
        this.modelMapper = modelMapper;
    }

    // 관리자용 게시글 목록 조회
    @Transactional(readOnly = true)
    public Map<String, Object> getAdminBoardList(String filter, String keyword, int size, int offset) {
        List<BoardVO> boards;
        int totalRecords;

        // 검색 조건 확인
        if (filter != null && keyword != null && !keyword.isBlank()) {
            // 검색 조건이 있는 경우
            boards = boardMapper.selectSearchBoardList(filter, keyword, size, offset);
            totalRecords = boardMapper.selectSearchTotalCount(filter, keyword);
        } else {
            // 검색 조건이 없는 경우
            boards = boardMapper.selectBoardList(size, offset);
            totalRecords = boardMapper.selectBoardTotalCount();
        }

        // VO → DTO 변환
        List<BoardDTO> boardListDTO = boards.stream()
                .map(vo -> modelMapper.map(vo, BoardDTO.class))
                .collect(Collectors.toList());

        // 총 페이지 계산
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        // 결과 맵 구성
        Map<String, Object> result = new HashMap<>();
        result.put("boardList", boardListDTO);    // 현재 페이지 데이터
        result.put("totalRecords", totalRecords); // 총 데이터 수
        result.put("totalPages", totalPages);     // 총 페이지 수
        result.put("currentPage", offset / size + 1); // 현재 페이지 번호

        return result;
    }
}
