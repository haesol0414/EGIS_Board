package com.example.board.dto.request;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpDTO {
    private String userId;
    private String userName;
    private String password;
}
