package com.example.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class UpdateBoardDTO {
    private String updateUserId;
    private String updateUserName;
    private String subject;
    private String contentText;
}
