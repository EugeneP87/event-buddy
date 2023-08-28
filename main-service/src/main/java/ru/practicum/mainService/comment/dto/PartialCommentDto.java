package ru.practicum.mainService.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainService.comment.CommentStateAction;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartialCommentDto {

    @NotBlank
    @Size(min = 20)
    private String text;
    @NotNull
    private CommentStateAction state;

}