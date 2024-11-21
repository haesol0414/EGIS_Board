package com.example.board.dto.request;

import lombok.*;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardUpdateDTO {
    private Long boardNo;
    private String updateUserId;
    private String subject;
    private String contentText;
    private Date updatedAt;
}
