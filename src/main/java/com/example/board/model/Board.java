package com.example.board.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class Board {
    private Long boardNo;        // default값 o
    private String createUser;   // user와 조인
    private String updateUser;   // user와 조인
    private String subject;      // not null
    private String contentText;  // not null
    private Integer viewCnt;     // default값 o
    private String createdAt;    // default값 o
    private String updatedAt;    // default값 o
    private Boolean deletedYn;   // default값 o
    private Integer groupNo;     // default값 o
    private Integer groupOrd;    // default값 o
    private Integer groupDep;    // default값 o
}

