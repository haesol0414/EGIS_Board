package com.example.board.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDetailDTO {
    private Long boardNo;
    private String createUserId;
    private String createUserName;
    private String updateUserId;
    private String updateUserName;
    private String subject;
    private String contentText;
    private Integer viewCnt;
    private Date createdAt;
    private Date updatedAt;
    private String deletedYn;
    private Integer groupNo;
    private Integer groupOrd;
    private Integer groupDep;
}
