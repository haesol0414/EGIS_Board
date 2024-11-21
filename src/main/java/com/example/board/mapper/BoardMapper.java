package com.example.board.mapper;

import com.example.board.vo.BoardVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BoardMapper {
    // 게시글 전체 조회
    @Select("""
                SELECT b.board_no AS boardNo,
                    b.create_user_id AS createUserId,
                    cu.user_name AS createUserName,
                    b.update_user_id AS updateUserId,
                    uu.user_name AS updateUserName,
                    b.subject,
                    b.view_cnt AS viewCnt,
                    b.created_at AS createdAt,
                    b.updated_at AS updatedAt,
                    b.deleted_yn AS deletedYn,
                    b.group_no AS groupNo,
                    b.group_ord AS groupOrd,
                    b.group_dep AS groupDep
                FROM board b
                LEFT JOIN users cu ON b.create_user_id = cu.user_id
                LEFT JOIN users uu ON b.update_user_id = uu.user_id
                WHERE deleted_yn = 'N'
                ORDER BY b.board_no DESC
                LIMIT #{size} OFFSET #{offset}
            """)
    List<BoardVO> getBoardList(@Param("size") int size, @Param("offset") int offset);

    // 전체 게시글 수 조회
    @Select("SELECT COUNT(*) FROM board")
    int getTotalBoardCount();

    // 게시글 상세조회
    @Select("""
                SELECT b.board_no AS boardNo,
                    b.create_user_id AS createUserId,
                    cu.user_name AS createUserName,
                    b.update_user_id AS updateUserId,
                    uu.user_name AS updateUserName,
                    b.subject,
                    b.content_text AS contentText,
                    b.view_cnt AS viewCnt,
                    b.created_at AS createdAt,
                    b.updated_at AS updatedAt,
                    b.deleted_yn AS deletedYn,
                    b.group_no AS groupNo,
                    b.group_ord AS groupOrd,
                    b.group_dep AS groupDep
                FROM board b
                LEFT JOIN users cu ON b.create_user_id = cu.user_id
                LEFT JOIN users uu ON b.update_user_id = uu.user_id
                WHERE board_no = #{boardNo};
            """)
    BoardVO getBoardDetail(Long boardNo);

    // 게시글 작성
    @Insert("INSERT INTO board (create_user_id, subject, content_text) VALUES (#{createUserId}, #{subject}, #{contentText})")
    void insertBoard(BoardVO newBoard);

    // 게시글 수정
    @Update("""
                UPDATE board
                SET
                    update_user_id = #{updateUserId},
                    subject = #{subject},
                    content_text = #{contentText},
                    updated_at = #{updatedAt}
                WHERE board_no = #{boardNo}
            """)
    void updateBoard(BoardVO updatedBoard);

    // 게시글 삭제
    @Delete("UPDATE board SET deleted_yn = 'Y' WHERE board_no = #{boardNo}")
    void deleteBoard(Long boardNo);

    // 조회수 증가
    @Update("UPDATE board SET view_cnt = view_cnt + 1 WHERE board_no = #{boardNo}")
    void updateViewCnt(Long boardNo);
}
