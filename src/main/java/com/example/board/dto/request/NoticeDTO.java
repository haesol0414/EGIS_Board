package com.example.board.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class NoticeDTO {
    private String isNotice;
    private Date startDate;
    private Date endDate;
}
