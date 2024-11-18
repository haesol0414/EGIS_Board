package com.example.board.dto;

import lombok.*;

import java.util.Date;

@Getter
@NoArgsConstructor
public class BoardListDTO {
    private Long boardNo;
    private String createUserId;
    private String createUserName;
    private String updateUserId;
    private String updateUserName;
    private String subject;
    private Integer viewCnt;
    private Date createdAt;
    private Date updatedAt;
    private Boolean deletedYn;
    private Integer groupNo;
    private Integer groupOrd;
    private Integer groupDep;
}
