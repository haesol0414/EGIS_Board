package com.example.board.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    private String userId;
    private String password;
}
