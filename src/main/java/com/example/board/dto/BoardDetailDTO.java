package com.example.board.dto;

import lombok.*;

import java.util.Date;

@Getter
@NoArgsConstructor
@ToString
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
    private Integer groupNo;
    private Integer groupOrd;
    private Integer groupDep;
}
