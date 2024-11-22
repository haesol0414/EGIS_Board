package com.example.board.mapper;

import com.example.board.vo.BoardVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardVO> selectBoardList(@Param("size") int size, @Param("offset") int offset);

    int selectBoardTotalCount();

    BoardVO selectBoardDetail(@Param("boardNo") Long boardNo);

    void insertBoard(BoardVO newBoard);

    void updateBoard(BoardVO updatedBoard);

    void deleteBoard(@Param("boardNo") Long boardNo);

    void updateViewCnt(@Param("boardNo") Long boardNo);

    List<BoardVO> searchBoardList(
            @Param("filter") String filter,
            @Param("keyword") String keyword,
            @Param("size") int size,
            @Param("offset") int offset
    );

    int selectSearchTotalCount(@Param("filter") String filter, @Param("keyword") String keyword);
}
