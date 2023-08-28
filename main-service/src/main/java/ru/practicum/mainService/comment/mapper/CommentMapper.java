package ru.practicum.mainService.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainService.comment.Comment;
import ru.practicum.mainService.comment.CommentState;
import ru.practicum.mainService.comment.dto.CommentDto;
import ru.practicum.mainService.comment.dto.NewCommentDto;
import ru.practicum.mainService.event.Event;
import ru.practicum.mainService.user.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated(),
                comment.getState()
        );
    }

    public Comment toComment(NewCommentDto commentDto, User user, Event event) {
        return new Comment(
                user,
                commentDto.getText(),
                event,
                LocalDateTime.now(),
                CommentState.PENDING
        );
    }

}