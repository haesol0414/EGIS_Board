package com.example.board.dto.request;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardUpdateDTO {
    private String updateUserId;
    private Long boardNo;
    private String subject;
    private String contentText;
    private Date updatedAt;
    private List<Long> removedFileIds;
}
