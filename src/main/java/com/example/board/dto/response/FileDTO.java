package com.example.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class FileDTO {
    private Long attachmentId;
    private Long boardNo;
    private String originFileName;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private Date uploadedAt;
    private String deletedYn;
}
