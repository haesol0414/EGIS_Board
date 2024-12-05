package com.example.board.vo;

import lombok.*;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardVO {
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
    private String isNotice;
    private Date startDate;
    private Date endDate;
}

