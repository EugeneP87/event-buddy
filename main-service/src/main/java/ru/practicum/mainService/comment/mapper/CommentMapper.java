package ru.practicum.mainService.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.mainService.comment.Comment;
import ru.practicum.mainService.comment.CommentState;
import ru.practicum.mainService.comment.dto.CommentDto;
import ru.practicum.mainService.comment.dto.NewCommentDto;
import ru.practicum.mainService.event.Event;
import ru.practicum.mainService.user.User;

@UtilityClass
public class CommentMapper {

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(comment.getAuthor().getName())
                .created(comment.getCreated())
                .state(comment.getState())
                .build();
    }

    public Comment toComment(NewCommentDto commentDto, User user, Event event) {
        return Comment.builder()
                .author(user)
                .text(commentDto.getText())
                .event(event)
                .state(CommentState.PENDING)
                .build();
    }

}