package com.example.board.mapper;

import com.example.board.dto.BoardDetailDTO;
import com.example.board.dto.BoardListDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BoardMapper {
    // 게시글 전체 조회
    @Select("""
                SELECT b.board_no AS boardNo,
                    b.create_user AS createUserId,
                    cu.user_name AS createUserName,
                    b.update_user AS updateUserId,
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
                LEFT JOIN users cu ON b.create_user = cu.user_id
                LEFT JOIN users uu ON b.update_user = uu.user_id
                ORDER BY b.board_no DESC
            """)
    List<BoardListDTO> getBoardList();

    // 게시글 상세조회
    @Select("""
                SELECT b.board_no AS boardNo,
                    b.create_user AS createUserId,
                    cu.user_name AS createUserName,
                    b.update_user AS updateUserId,
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
                LEFT JOIN users cu ON b.create_user = cu.user_id
                LEFT JOIN users uu ON b.update_user = uu.user_id
                WHERE board_no = #{boardNo}
    
            """)
    BoardDetailDTO getBoardDetail(Long boardNo);
}
