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
import java.util.*;
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
    public Map<String, Object> getBoardList(String filter, String keyword, int size, int offset) {
        List<BoardVO> boards;
        int totalRecords;

        // 검색 조건 확인
        if (filter != null && keyword != null && !keyword.isBlank()) {
            // 검색 조건이 있는 경우
            boards = boardMapper.searchBoardList(filter, keyword, size, offset);
            totalRecords = boardMapper.selectSearchTotalCount(filter, keyword);
        } else {
            // 검색 조건이 없는 경우
            boards = boardMapper.selectBoardList(size, offset);
            totalRecords = boardMapper.selectBoardTotalCount();
        }

        // 총 페이지 계산
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        // 결과 맵 구성
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
    public Long createBoard(BoardCreateDTO boardCreateDTO, List<MultipartFile> files) {
        // DTO → VO 변환 후 insert
        BoardVO newBoard = modelMapper.map(boardCreateDTO, BoardVO.class);
        Long boardNo = boardMapper.insertBoard(newBoard);

        System.out.println(files);

        // 파일이 존재할 경우 업로드 처리
        if (files != null && !files.isEmpty()) {
            uploadFiles(files, boardNo);
        }

        return boardNo;
    }


    // 게시글 수정
    @Transactional
    public void updateBoard(Long boardNo, BoardUpdateDTO boardUpdateDTO, List<MultipartFile> files) {
        BoardVO updatedBoard = modelMapper.map(boardUpdateDTO, BoardVO.class);
        boardMapper.updateBoard(updatedBoard);

        System.out.println(boardNo);
        // 삭제된 파일 처리
        if (boardUpdateDTO.getRemovedFileIds() != null && !boardUpdateDTO.getRemovedFileIds().isEmpty()) {
            System.out.println(boardUpdateDTO.getRemovedFileIds());
            boardMapper.deleteFilesByIds(boardUpdateDTO.getRemovedFileIds());
        }

        // 파일이 존재할 경우 업로드 처리
        if (files != null && !files.isEmpty()) {
            uploadFiles(files, boardNo);
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
    public Long addReply(Long boardNo, BoardReplyDTO boardReplyDTO, List<MultipartFile> files) {
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

        // 파일이 존재할 경우 업로드 처리
        if (files != null && !files.isEmpty()) {
            uploadFiles(files, boardNo);
        }

        return replyNo;
    }

    // 파일 저장
    @Transactional
    public void uploadFiles(List<MultipartFile> files, Long boardNo) {
        List<FileVO> fileVOList = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                String originFileName = file.getOriginalFilename();
                String savedFileName = UUID.randomUUID() + "_" + originFileName;
                String filePath = "D:\\uploads\\" + savedFileName;

                // 파일 저장
                Files.copy(file.getInputStream(), Paths.get(filePath));

                // FileVO 생성
                FileVO attachment = FileVO.builder()
                        .boardNo(boardNo)
                        .originFileName(originFileName)
                        .fileName(savedFileName)
                        .filePath(filePath)
                        .fileSize(file.getSize())
                        .build();

                fileVOList.add(attachment);
            } catch (IOException e) {
                throw new RuntimeException("파일 업로드 실패", e);
            }
        }

        // 매퍼 호출로 일괄 삽입
        if (!fileVOList.isEmpty()) {
            boardMapper.insertFiles(fileVOList);
        }
    }

    // 파일 조회
    @Transactional(readOnly = true)
    public List<FileDTO> getFilesByBoardNo(Long boardNo) {
        List<FileVO> fileList = boardMapper.selectFilesByBoardNo(boardNo);

        // 파일이 없을 경우 빈 리스트 반환
        if (fileList == null || fileList.isEmpty()) {
            return Collections.emptyList();
        }

        return fileList.stream()
                .map(fileVO -> modelMapper.map(fileVO, FileDTO.class))
                .collect(Collectors.toList());
    }

    // 파일 상세 정보 조회
    public FileDTO getFileDetails(Long attachmentId) {
        FileVO fileVO = boardMapper.selectFileById(attachmentId);
        if (fileVO == null) {
            throw new IllegalArgumentException("파일 정보를 찾을 수 없습니다.");
        }

        return modelMapper.map(fileVO, FileDTO.class);
    }
}