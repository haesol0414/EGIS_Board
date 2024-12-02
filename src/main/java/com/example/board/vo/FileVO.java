package com.example.board.vo;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class FileVO {
    private Long attachmentId;         // 첨부파일 고유 ID
    private Long boardNo;              // 게시글 ID (외래 키)
    private String originFileName;     // 원본 파일 이름
    private String fileName;           // 저장된 파일 이름
    private String filePath;           // 파일 저장 경로
    private Long fileSize;             // 파일 크기
    private Date uploadedAt;
    private String deletedYn;
}
