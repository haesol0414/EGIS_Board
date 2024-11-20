package com.example.board.dto;

import lombok.*;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardUpdateDTO {
    private Long boardNo;
    private String updateUserId;
    private String subject;
    private String contentText;
    private Date updatedAt;
}
