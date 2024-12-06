package com.example.board.dto.request;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardUpdateDTO {
    private String updateUserId;
    private Long boardNo;
    private String subject;
    private String contentText;
    private Date updatedAt;
    private List<Long> removedFileIds;
    private String isNotice;
    private Date startDate;
    private Date endDate;
}
