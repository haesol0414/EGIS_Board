package com.example.board.mapper;

import com.example.board.vo.BoardVO;
import com.example.board.vo.FileVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BoardMapper {
    // 게시글 목록 조회
//    List<BoardVO> selectBoardList(@Param("size") int size, @Param("offset") int offset);
//
//    // 전체 게시글 갯수
//    int selectBoardTotalCount();

    // 게시글 상세 조회
    BoardVO selectBoardDetail(@Param("boardNo") Long boardNo);

    // 게시글 작성
    Long insertBoard(BoardVO newBoard);

    // 게시글 수정
    void updateBoard(BoardVO updatedBoard);

    // 게시글 삭제
    void deleteBoard(@Param("boardNo") Long boardNo);

    // 조회수 증가
    void updateViewCnt(@Param("boardNo") Long boardNo);

//    // 게시글 검색
//    List<BoardVO> searchBoardList(
//            @Param("filter") String filter,
//            @Param("keyword") String keyword,
//            @Param("size") int size,
//            @Param("offset") int offset
//    );
//
//    // 검색된 게시글 갯수
//    int selectSearchTotalCount(@Param("filter") String filter, @Param("keyword") String keyword);
    List<BoardVO> selectBoardList(@Param("size") int size, @Param("offset") int offset);

    int selectBoardTotalCount();

    List<BoardVO> searchBoardList(@Param("filter") String filter, @Param("keyword") String keyword,
                                  @Param("size") int size, @Param("offset") int offset);

    int selectSearchTotalCount(@Param("filter") String filter, @Param("keyword") String keyword);


    // 답글 작성
    Long insertReply(BoardVO newReply);

    // 답글 순서 관리
    void updateGroupOrd(@Param("groupNo") Integer groupNo, @Param("parentOrd") Integer parentOrd);

    // 첨부파일 저장    
    void insertFiles(@Param("list") List<FileVO> files);

    // 게시글 첨부파일 조회
    List<FileVO> selectFilesByBoardNo(Long boardNo);

    // 파일 아이디로 조회
    FileVO selectFileById(@Param("attachmentId") Long attachmentId);

    // 첨부파일 삭제
    void deleteFilesByIds(List<Long> deletedFileIds);
}