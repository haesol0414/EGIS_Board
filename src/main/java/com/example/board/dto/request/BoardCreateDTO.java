package com.example.board.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardCreateDTO {
    private String createUserId;
    private String subject;
    private String contentText;
}
