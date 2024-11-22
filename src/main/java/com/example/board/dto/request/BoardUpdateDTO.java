package com.example.board.dto.request;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardUpdateDTO {
    private Long boardNo;
    private String updateUserId;
    private String subject;
    private String contentText;
    private Date updatedAt;
}
