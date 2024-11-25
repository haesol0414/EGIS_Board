package com.example.board.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardReplyDTO {
    private String createUserId;
    private String subject;
    private String contentText;
    private Integer groupNo;
    private Integer groupOrd;
    private Integer groupDep;
}
