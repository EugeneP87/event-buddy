package ru.practicum.mainService.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainService.comment.CommentState;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Long id;
    private String text;
    private String author;
    private LocalDateTime created;
    private CommentState state;

}