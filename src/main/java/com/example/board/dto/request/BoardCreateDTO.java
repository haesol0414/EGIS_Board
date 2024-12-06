package com.example.board.dto.request;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class BoardCreateDTO {
    private String createUserId;
    private String subject;
    private String contentText;
    private String isNotice;
    private Date startDate;
    private Date endDate;
}
