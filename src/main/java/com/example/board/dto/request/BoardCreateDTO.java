package com.example.board.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardCreateDTO {
    private String createUserId;
    private String subject;
    private String contentText;
}
