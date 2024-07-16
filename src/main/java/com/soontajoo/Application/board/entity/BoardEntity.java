package com.soontajoo.Application.board.entity;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "board")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Integer boardId;

    @Column(name = "board_title")
    private String title;

    @Column(name = "board_createdat")
    @Timestamp
    private LocalDate createdAt;

    @Column(name = "board_updatedat")
    @CreationTimestamp
    private LocalDate updatedAt;

    @Column(name = "board_deletedat")
    @CreationTimestamp
    private LocalDate deletedAt;

    @Column(name = "board_author")
    private String author;

    @Column(name = "board_viewcount")
    private Integer viewCount;

    @Column(name = "board_content", columnDefinition = "TEXT")
    private String content;
}
