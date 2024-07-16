package com.soontajoo.Application.board.dto;

import lombok.*;

import java.time.LocalDate;

public class BoardDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Request {
        // 요청에 사용되는 DTO 클래스
        private Integer boardId;
        private String title;
        private String content;
        private String author;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Response {
        // 응답에 사용되는 DTO 클래스
        private Integer boardId;
        private String title;
        private String content;
        private String statusMessage;
        private String author;
        private LocalDate createdTime;
    }
}
