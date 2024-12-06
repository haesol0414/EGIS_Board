package com.example.board.service.impl;

import com.example.board.dto.request.BoardReplyDTO;
import com.example.board.dto.response.BoardDTO;
import com.example.board.dto.request.BoardCreateDTO;
import com.example.board.dto.request.BoardUpdateDTO;
import com.example.board.dto.response.FileDTO;
import com.example.board.mapper.BoardMapper;
import com.example.board.vo.BoardVO;
import com.example.board.service.BoardService;
import com.example.board.vo.FileVO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getBoardList(String filter, String keyword, int size, int offset) {
        List<BoardVO> boards;

        // 검색 조건 확인
        if (filter != null && keyword != null && !keyword.isBlank()) {
            // 검색 조건이 있는 경우
            boards = boardMapper.selectSearchBoardList(filter, keyword, Integer.MAX_VALUE, 0); // 전체 데이터 조회
        } else {
            // 검색 조건이 없는 경우
            boards = boardMapper.selectBoardList(Integer.MAX_VALUE, 0); // 전체 데이터 조회
        }

        // VO → DTO 변환 및 삭제된 게시글과 답글 처리
        List<BoardDTO> boardListDTO = boards.stream()
                .map(vo -> {
                    BoardDTO dto = modelMapper.map(vo, BoardDTO.class);
                    // 답글이 달려있는지 체크
                    boolean hasReplies = boardMapper.checkHasReplies(vo.getGroupNo(), vo.getGroupOrd(), vo.getGroupDep());
                    dto.setHasReplies(hasReplies);
                    return dto;
                })
                // 삭제된 게시글이고 답글도 없는 경우 리스트에서 제외
                .filter(dto -> !(dto.getDeletedYn().equals("Y") && !dto.isHasReplies()))
                .collect(Collectors.toList());

        // 필터링된 데이터에서 페이지네이션 적용
        int totalRecords = boardListDTO.size();
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        List<BoardDTO> paginatedBoardList = boardListDTO.stream()
                .skip(offset)
                .limit(size)
                .collect(Collectors.toList());

        // 결과 맵 구성
        Map<String, Object> result = new HashMap<>();
        result.put("boardList", paginatedBoardList); // 현재 페이지 데이터
        result.put("totalRecords", totalRecords);   // 필터링된 데이터 기준 총 데이터 수
        result.put("totalPages", totalPages);       // 필터링된 데이터 기준 총 페이지 수
        result.put("currentPage", offset / size + 1); // 현재 페이지 번호

        return result;
    }

    // 게시글 상세 조회
    @Override
    @Transactional(readOnly = true)
    public BoardDTO getBoardDetail(Long boardNo) {
        // VO 객체 조회
        BoardVO board = boardMapper.selectBoardDetail(boardNo);

        // VO → DTO 변환
        return modelMapper.map(board, BoardDTO.class);
    }

    // 게시글 작성
    @Override
    @Transactional
    public Long createBoard(BoardCreateDTO boardCreateDTO, List<MultipartFile> files) {
        // DTO → VO 변환
        BoardVO newBoard = modelMapper.map(boardCreateDTO, BoardVO.class);

        // 게시글 작성
        Long boardNo = boardMapper.insertBoard(newBoard);

        // 파일이 존재할 경우 업로드 처리
        if (files != null && !files.isEmpty()) {
            uploadFiles(files, boardNo);
        }

        return boardNo;
    }


    // 게시글 수정
    @Override
    @Transactional
    public void updateBoard(Long boardNo, BoardUpdateDTO boardUpdateDTO, List<MultipartFile> files) {
        BoardVO updatedBoard = modelMapper.map(boardUpdateDTO, BoardVO.class);
        boardMapper.updateBoard(updatedBoard);

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
    @Override
    @Transactional
    public void deleteBoard(Long boardNo) {
        // 삭제 대상 게시글 정보 가져오기
        BoardVO board = boardMapper.selectBoardDetail(boardNo);
        if (board == null) {
            throw new IllegalArgumentException("삭제하려는 게시글이 존재하지 않습니다.");
        }

        // 파일 존재 여부 확인
        List<FileDTO> fileList = getFilesByBoardNo(boardNo);
        if (!fileList.isEmpty()) {
            // 파일 삭제
            boardMapper.deleteAttachmentsByBoardNo(boardNo);
        }

        // 게시글 삭제
        boardMapper.deleteBoard(boardNo);
    }

    // 조회수 증가
    @Override
    @Transactional
    public void updateViewCnt(Long boardNo) {
        boardMapper.updateViewCnt(boardNo);
    }

    // 답글 작성
    @Override
    @Transactional
    public Long addReply(Long parentBoardNo, BoardReplyDTO boardReplyDTO, List<MultipartFile> files) {
        // 부모 글 정보 조회
        BoardVO parent = boardMapper.selectBoardDetail(parentBoardNo);
        if (parent == null) {
            throw new IllegalArgumentException("원글이 존재하지 않습니다.");
        }

        // 삽입 위치 결정
        Integer nextGroupOrd = boardMapper.findNextGroupOrd(
                parent.getGroupNo(), parent.getGroupOrd(), parent.getGroupDep()
        );

        Integer newGroupOrd;
        if (nextGroupOrd == 0) {
            // 그룹 내 가장 마지막 위치로 설정
            newGroupOrd = boardMapper.findMaxGroupOrd(parent.getGroupNo());
        } else {
            // 기존 글 순서를 밀어낸 후 삽입 위치로 설정
            boardMapper.updateGroupOrd(parent.getGroupNo(), nextGroupOrd);
            newGroupOrd = nextGroupOrd;
        }

        // 새 답글의 그룹 깊이 설정
        int newGroupDep = parent.getGroupDep() + 1;

        boardReplyDTO.setGroupNo(parent.getGroupNo());
        boardReplyDTO.setGroupOrd(newGroupOrd);
        boardReplyDTO.setGroupDep(newGroupDep);

        // DTO → VO 변환 후 insert
        BoardVO newReply = modelMapper.map(boardReplyDTO, BoardVO.class);
        Long replyNo = boardMapper.insertReply(newReply);

        // 파일이 존재할 경우 업로드 처리
        if (files != null && !files.isEmpty()) {
            uploadFiles(files, replyNo);
        }

        return replyNo;
    }

    // 파일 저장
    @Override
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

    // 게시글 파일들 조회
    @Override
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
    @Override
    public FileDTO getFileDetails(Long attachmentId) {
        FileVO fileVO = boardMapper.selectFileById(attachmentId);
        if (fileVO == null) {
            throw new IllegalArgumentException("파일 정보를 찾을 수 없습니다.");
        }

        return modelMapper.map(fileVO, FileDTO.class);
    }

    // 공지사항 조회
    @Override
    @Transactional
    public List<BoardDTO> getNoticeList() {
        List<BoardVO> notices = boardMapper.findNotices();

        return notices.stream()
                .map(notice -> modelMapper.map(notice, BoardDTO.class))
                .collect(Collectors.toList());
    }
}