package com.example.board.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class BoardReplyDTO {
    private String createUserId;
    private String subject;
    private String contentText;
    private Integer groupNo;
    private Integer groupOrd;
    private Integer groupDep;
}
