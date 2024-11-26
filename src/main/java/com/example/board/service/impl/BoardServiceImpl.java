package com.example.board.service.impl;

import com.example.board.dto.request.BoardReplyDTO;
import com.example.board.dto.response.BoardDetailDTO;
import com.example.board.dto.request.BoardCreateDTO;
import com.example.board.dto.request.BoardUpdateDTO;
import com.example.board.dto.response.FileDTO;
import com.example.board.mapper.BoardMapper;
import com.example.board.vo.BoardVO;
import com.example.board.service.BoardService;
import com.example.board.vo.FileVO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    public Long createBoard(BoardCreateDTO boardCreateDTO, MultipartFile file) {
        System.out.println(boardCreateDTO);

        // DTO → VO 변환 후 insert
        BoardVO newBoard = modelMapper.map(boardCreateDTO, BoardVO.class);
        Long boardNo = boardMapper.insertBoard(newBoard);

        if (file != null && !file.isEmpty()) {
            uploadFile(file, boardNo);
        }

        return boardNo;
    }

    // 게시글 수정
    @Transactional
    public void updateBoard(Long boardNo, BoardUpdateDTO boardUpdateDTO, MultipartFile file) {
        // DTO → VO 변환 후 update
        BoardVO updatedBoard = modelMapper.map(boardUpdateDTO, BoardVO.class);
        boardMapper.updateBoard(updatedBoard);

        if (file == null || file.isEmpty()) {
            // 파일이 없을 경우 기존 파일 삭제
            boardMapper.deleteFileByBoardNo(boardNo);
        } else {
            // 파일이 존재하면 기존 파일 삭제 후 새 파일 업로드
            boardMapper.deleteFileByBoardNo(boardNo);
            uploadFile(file, boardNo);
        }
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
    public Long addReply(Long boardNo, BoardReplyDTO boardReplyDTO, MultipartFile file) {
        // 부모 글 정보 조회
        BoardVO parent = boardMapper.selectBoardDetail(boardNo);
        if (parent == null) {
            throw new IllegalArgumentException("원글이 존재하지 않습니다.");
        }

        // 그룹 정보 설정
        boardReplyDTO.setGroupNo(parent.getGroupNo());                     // 부모 글의 그룹 번호 상속
        boardReplyDTO.setGroupOrd(parent.getGroupOrd() + 1);               // 부모 글의 다음 순서
        boardReplyDTO.setGroupDep(parent.getGroupDep() + 1);               // 부모 글의 깊이 + 1

        // 기존 글 순서 밀기
        boardMapper.updateGroupOrd(parent.getGroupNo(), parent.getGroupOrd());

        // DTO → VO 변환 후 insert
        BoardVO newReply = modelMapper.map(boardReplyDTO, BoardVO.class);
        Long replyNo = boardMapper.insertReply(newReply);
        System.out.println(replyNo);

        if (file != null && !file.isEmpty()) {
            uploadFile(file, replyNo);
        }

        return replyNo;
    }

    // 파일 저장
    @Transactional
    public void uploadFile(MultipartFile file, Long boardNo) {
        try {
            String originFileName = file.getOriginalFilename();
            String savedFileName = UUID.randomUUID() + "_" + originFileName;
            String filePath = "D:\\uploads\\" + savedFileName;

            // 파일 저장
            Files.copy(file.getInputStream(), Paths.get(filePath));

            // FileVO 생성 및 저장
            FileVO attachment = FileVO.builder()
                    .boardNo(boardNo)
                    .originFileName(originFileName)
                    .fileName(savedFileName)
                    .filePath(filePath)
                    .fileSize(file.getSize())
                    .build();

            boardMapper.insertAttachment(attachment);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }

    @Transactional
    public FileDTO getFileByBoardNo(Long boardNo) {
        FileVO fileVO = boardMapper.selectFileByBoardNo(boardNo);
        if (fileVO == null) {
            return null;
        }

        // VO → DTO 변환
        return modelMapper.map(fileVO, FileDTO.class);
    }
}
