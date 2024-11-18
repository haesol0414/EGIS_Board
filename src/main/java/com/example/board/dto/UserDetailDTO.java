package com.example.board.dto;

import com.example.board.model.User;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDTO {
    private String userId;
    private String userName;
    private String role;

    public static UserDetailDTO fromEntity(User user) {
        return new UserDetailDTO(user.getUserId(), user.getUserName(), user.getRole().name());
    }
}
