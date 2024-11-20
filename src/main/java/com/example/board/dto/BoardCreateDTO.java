package com.example.board.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardCreateDTO {
    private String createUserId;
    private String subject;
    private String contentText;
}
